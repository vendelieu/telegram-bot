package eu.vendeli.tgbot.core

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.internal.ExperimentalFeature
import eu.vendeli.tgbot.annotations.internal.KtGramInternal
import eu.vendeli.tgbot.types.component.*
import eu.vendeli.tgbot.utils.common.*
import eu.vendeli.tgbot.utils.internal.toJsonElement
import io.ktor.client.plugins.*
import io.ktor.util.logging.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * An update processing class.
 *
 * @property bot bot instance.
 */
class TgUpdateHandler internal constructor(
    internal val bot: TelegramBot,
) {
    val registry = ActivityRegistry()
    val pipeline = ProcessingPipeline()
    private var handlingBehaviour: HandlingBehaviourBlock = DEFAULT_HANDLING_BEHAVIOUR

    private val updatesFlow = MutableSharedFlow<ProcessedUpdate>(extraBufferCapacity = 10)
    private val logger = bot.config.loggerFactory.get(this::class.fqName)

    internal val handlerJob = Job(bot.rootJob)
    internal val handlerScope = bot.config.updatesListener.run {
        CoroutineScope(handlerJob + dispatcher + CoroutineName("TgBot"))
    }
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

    private var processingEx: TgException? = null
    private fun collectUpdates(types: List<UpdateType>?) = handlerScope.launch(Job(handlerJob)) {
        val cfg = bot.config.updatesListener
        logger.debug { "Starting updates collector." }

        var lastUpdateId = 0
        val getUpdatesAction = GET_UPDATES_ACTION.options {
            allowedUpdates = types
            timeout = cfg.updatesPollingTimeout
        }

        while (isActive) {
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
                cfg.pullingDelay.takeIf { it > 0 }?.let { delay(it) }
            } catch (e: HttpRequestTimeoutException) {
                stopListener()
                processingEx = TgException("Connection timeout", e)
            }
        }
    }

    private fun processUpdates() = handlerScope.launch {
        updatesFlow.collect { update ->
            launch(bot.config.updatesListener.processingDispatcher) {
                handlingBehaviour(this@TgUpdateHandler, update)
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
        collectUpdates(allowedUpdates)
        logger.info("Starting long-polling listener.")
        processUpdates().join()
        processingEx?.also { throw it }
    }

    /**
     * Stops listening of new updates.
     *
     */
    fun stopListener() {
        handlerJob.cancelChildren()
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
    fun parseAndHandle(update: String): Job = parse(update).let {
        logger.debug { "Processing update with preset behaviour." }
        handlerScope.launch(bot.config.updatesListener.processingDispatcher) {
            handlingBehaviour(this@TgUpdateHandler, it.await() ?: return@launch)
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

    suspend fun setFunctionality(block: FunctionalHandlingBlock) {
        functionalDsl.block()
        functionalDsl.apply()
    }

    /**
     * Handle update through the unified pipeline.
     * Works for both annotation-based and functional handlers.
     */
    suspend fun handle(update: ProcessedUpdate): Unit =
        pipeline.execute(ProcessingContext(update, bot, registry))

    companion object
}
