package eu.vendeli.tgbot.core

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.dsl.BotDSL
import eu.vendeli.tgbot.annotations.internal.ExperimentalFeature
import eu.vendeli.tgbot.annotations.internal.KtGramInternal
import eu.vendeli.tgbot.types.component.*
import eu.vendeli.tgbot.utils.common.*
import eu.vendeli.tgbot.utils.internal.toJsonElement
import io.ktor.client.network.sockets.*
import io.ktor.client.plugins.*
import io.ktor.util.logging.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.selects.select
import kotlinx.serialization.SerializationException
import kotlin.coroutines.coroutineContext
import kotlin.time.Duration.Companion.milliseconds

/**
 * An update processing class.
 *
 * @property bot bot instance.
 */
@BotDSL
class TgUpdateHandler internal constructor(
    internal val bot: TelegramBot,
) {
    val registry = ActivityRegistry()
    val pipeline = ProcessingPipeline()
    private var handlingBehaviour: HandlingBehaviourBlock = DEFAULT_HANDLING_BEHAVIOUR

    private val updatesFlow = MutableSharedFlow<ProcessedUpdate>(extraBufferCapacity = 10)
    private val logger = bot.config.loggerFactory.get(this::class.fqName)

    // Long-lived scope backing parse / parseAndHandle. SupervisorJob so a single handler throw
    // can't permanently break the bot for unrelated callers.
    internal val handlerJob = SupervisorJob(bot.rootJob)
    internal val handlerScope = bot.config.updatesListener.run {
        CoroutineScope(handlerJob + dispatcher + CoroutineName("TgBot"))
    }

    // Per-setListener job. Cancelled by stopListener; recreated on each setListener call.
    private var listenerJob: Job? = null

    internal val functionalDsl by lazy { FunctionalHandlingDsl(bot) }

    /**
     * The channel where errors caught during update processing is stored with update that caused them.
     */
    val caughtExceptions by lazy { Channel<FailedUpdate>(Channel.CONFLATED) }

    /**
     * Update flow being processed by the handler.
     * @since 7.4.0
     */
    @ExperimentalFeature
    val flow: Flow<ProcessedUpdate> get() = updatesFlow

    @KtGramInternal
    var ____ctxUtils: CtxUtils? = null

    private sealed interface PollOutcome {
        data class Retry(val reason: String) : PollOutcome
        data class Fatal(val ex: TgException) : PollOutcome
    }

    private fun classify(e: Throwable): PollOutcome = when (e) {
        is HttpRequestTimeoutException -> PollOutcome.Fatal(TgException("Connection timeout", e))
        is SocketTimeoutException -> PollOutcome.Retry("socket timeout")
        is ConnectTimeoutException -> PollOutcome.Retry("connect timeout")
        is SerializationException -> PollOutcome.Fatal(TgException("Update deserialization failed", e))
        is TgFailureException -> PollOutcome.Fatal(TgException("Telegram returned failure: ${e.message}", e))
        is ClientRequestException -> when (e.response.status.value) {
            TOO_MANY_REQUESTS -> PollOutcome.Retry("rate limited (429)")
            UNAUTHORIZED, FORBIDDEN -> PollOutcome.Fatal(
                TgException("Telegram rejected token (${e.response.status.value})", e),
            )

            else -> PollOutcome.Fatal(TgException("Telegram client error ${e.response.status.value}", e))
        }

        is ServerResponseException -> PollOutcome.Retry("server ${e.response.status.value}")
        else -> PollOutcome.Fatal(TgException("Unexpected polling failure", e))
    }

    private suspend fun runPollingLoop(
        types: List<UpdateType>?,
        pollResult: CompletableDeferred<TgException?>,
    ) {
        val cfg = bot.config.updatesListener
        var lastUpdateId = 0
        var backoff = INITIAL_BACKOFF_MS
        val getUpdatesAction = GET_UPDATES_ACTION.options {
            allowedUpdates = types
            timeout = cfg.updatesPollingTimeout
        }

        while (currentCoroutineContext().isActive) {
            logger.trace { "Running listener with offset - $lastUpdateId" }
            try {
                getUpdatesAction
                    .apply {
                        parameters["offset"] = lastUpdateId.toJsonElement()
                    }.sendReturning(bot)
                    .getOrNull()
                    ?.forEach {
                        updatesFlow.emit(it)
                        lastUpdateId = it.updateId + 1
                    }
                backoff = INITIAL_BACKOFF_MS
                cfg.pullingDelay.takeIf { it > 0 }?.let { delay(it.milliseconds) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: @Suppress("detekt:TooGenericExceptionCaught") Throwable) {
                when (val outcome = classify(e)) {
                    is PollOutcome.Retry -> {
                        logger.warn("Recoverable poll error: ${outcome.reason}. Backing off ${backoff}ms.")
                        delay(backoff.milliseconds)
                        backoff = (backoff * 2).coerceAtMost(MAX_BACKOFF_MS)
                    }

                    is PollOutcome.Fatal -> {
                        pollResult.complete(outcome.ex)
                        return
                    }
                }
            }
        }
        pollResult.complete(null)
    }

    private suspend fun runProcessingLoop(ready: CompletableDeferred<Unit>) {
        supervisorScope {
            updatesFlow
                .onSubscription { ready.complete(Unit) }
                .collect { update ->
                    launch(bot.config.updatesListener.processingDispatcher) {
                        handlingBehaviour(this@TgUpdateHandler, update)
                    }
                }
        }
    }

    /**
     * Function to define the actions that will be applied to updates when they are being processed.
     * When set, it starts an update processing cycle.
     *
     * @param block action that will be applied.
     */
    suspend fun setListener(allowedUpdates: List<UpdateType>? = null, block: HandlingBehaviourBlock) {
        stopListener()
        logger.debug { "The listener is set." }
        handlingBehaviour = block

        val session = SupervisorJob(handlerJob).also { listenerJob = it }
        val sessionScope = CoroutineScope(handlerScope.coroutineContext + session)
        val pollResult = CompletableDeferred<TgException?>()
        val ready = CompletableDeferred<Unit>()

        val processor = sessionScope.launch { runProcessingLoop(ready) }
        ready.await()
        sessionScope.launch { runPollingLoop(allowedUpdates, pollResult) }

        logger.info("Starting long-polling listener.")

        try {
            val err: TgException? = select {
                pollResult.onAwait { it }
                processor.onJoin { null }
            }
            err?.let { throw it }
        } finally {
            session.cancel()
            session.join()
            listenerJob = null
        }
    }

    /**
     * Stops listening of new updates.
     */
    fun stopListener() {
        listenerJob?.cancel()
        listenerJob = null
        logger.debug { "The listener is stopped." }
    }

    /**
     * A function for defining the behavior to handle updates.
     */
    fun setBehaviour(block: HandlingBehaviourBlock) {
        logger.debug { "Handling behaviour is set." }
        stopListener()
        handlingBehaviour = block
    }

    /**
     * A method for handling updates from a string.
     * Define processing behavior before calling, see [setBehaviour].
     */
    fun parseAndHandle(update: String): Job {
        val pinned = handlingBehaviour
        return parse(update).let {
            logger.debug { "Processing update with preset behaviour." }
            handlerScope.launch(bot.config.updatesListener.processingDispatcher) {
                pinned(this@TgUpdateHandler, it.await() ?: return@launch)
            }
        }
    }

    /**
     * A method to parse update from string.
     */
    fun parse(update: String): Deferred<ProcessedUpdate?> = handlerScope.async {
        logger.trace { "Trying to parse update from string - $update" }

        serde
            .runCatching { decodeFromString(ProcessedUpdate.serializer(), update) }
            .onFailure {
                logger.error("error during the update parsing process.", it)
            }.onSuccess { logger.debug { "Successfully parsed update to $it" } }
            .getOrNull()
    }

    /**
     * Handle update through the unified pipeline.
     * Works for both annotation-based and functional handlers.
     */
    suspend fun handle(update: ProcessedUpdate): Unit =
        pipeline.execute(ProcessingContext(update, bot, registry))

    companion object {
        private const val INITIAL_BACKOFF_MS = 500L
        private const val MAX_BACKOFF_MS = 30_000L
        private const val TOO_MANY_REQUESTS = 429
        private const val UNAUTHORIZED = 401
        private const val FORBIDDEN = 403
    }
}
