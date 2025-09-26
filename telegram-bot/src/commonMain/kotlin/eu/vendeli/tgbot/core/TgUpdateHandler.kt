package eu.vendeli.tgbot.core

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.internal.ExperimentalFeature
import eu.vendeli.tgbot.annotations.internal.KtGramInternal
import eu.vendeli.tgbot.types.component.*
import eu.vendeli.tgbot.utils.common.*
import eu.vendeli.tgbot.utils.internal.*
import eu.vendeli.tgbot.utils.internal.debug
import io.ktor.client.plugins.*
import io.ktor.util.logging.trace
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
    commandsPackage: String? = null,
    internal val bot: TelegramBot,
) {
    private val activities by lazy { ActivitiesData(commandsPackage, logger) }
    private var handlingBehaviour: HandlingBehaviourBlock = DEFAULT_HANDLING_BEHAVIOUR
    private val updatesFlow = MutableSharedFlow<ProcessedUpdate>(extraBufferCapacity = 10)

    internal val handlerJob = Job(bot.rootJob)
    internal val handlerScope = bot.config.updatesListener.run {
        CoroutineScope(handlerJob + dispatcher + CoroutineName("TgBot"))
    }
    internal val functionalHandlingBehavior by lazy { FunctionalHandlingDsl(bot) }
    internal val logger = getLogger(bot.config.logging.botLogLevel, this::class.fqName)

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

    /**
     * Previous invoked function qualified path (i.e., full class path).
     * @since 7.0.0
     */
    @KtGramInternal
    val userClassSteps = mutableMapOf<Long, String>()

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
        logger.info { "Starting long-polling listener." }
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
    fun parseAndHandle(update: String): Job? = parse(update).let {
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
                logger.error(it) { "error during the update parsing process." }
            }.onSuccess { logger.debug { "Successfully parsed update to $it" } }
            .getOrNull()
    }

    /**
     * Handle the update.
     *
     * @param update
     */
    suspend fun handle(update: ProcessedUpdate): Unit = update.run {
        if (!middlewarePreHandleShot(update)) return@run
        logger.trace { "Handling update: ${update.toJsonString()}\nProcessed into: $update" }
        val user = userOrNull
        // check general user limits
        if (checkIsLimited(bot.config.rateLimiter.limits, user?.id))
            return@run

        var request = parseCommand(text)
        var activityId = request.command

        // check parsed command existence
        var invocation: Invocable? = activities.commandHandlers[request.command to type]

        // if there's no command > check input point
        if (invocation == null && user != null)
            invocation = bot.inputListener.getAsync(user.id).await()?.let {
                request = parseCommand(it)
                activityId = request.command
                activities.inputHandlers[activityId]
            }

        // remove input listener point
        if (user != null && bot.config.inputAutoRemoval) bot.inputListener.del(user.id)

        val processingCtx = if (bot.config.processingCtxTargets.isNotEmpty()) {
            ProcessingCtx()
        } else ProcessingCtx.EMPTY

        // if there's no command and input > check common handlers
        if (invocation == null) invocation = activities.commonHandlers.entries
            .firstOrNull {
                it.key.match(request.command, this, bot, processingCtx)
            }?.also {
                activityId = it.key.value.toString()
            }?.value

        // check guard condition
        if (invocation?.second?.guard?.checkIsGuarded(user, this, bot) == false) {
            logger.debug { "Invocation guarded: ${invocation.second}" }
            return@run
        }

        // if we found any action > check for its limits
        if (invocation != null
            && checkIsLimited(invocation.second.rateLimits, user?.id, activityId)
        ) return@run

        val params = getParameters(invocation?.second?.argParser, request)

        logger.debug {
            "Result of parsing text: ${request.command} with parameters $params" +
                "\nResult of finding action - ${invocation?.second}"
        }

        if (!middlewarePreInvokeShot(update, processingCtx)) return@run
        // invoke update type handler if there's
        activities.updateTypeHandlers[type]?.invokeCatching(
            update = this,
            parameters = params,
            kind = TgInvocationKind.TYPE,
            processingCtx = processingCtx,
        )

        when {
            invocation != null -> invocation.first.invokeCatching(
                update = this,
                parameters = params,
                kind = TgInvocationKind.ACTIVITY,
                processingCtx = processingCtx,
                meta = invocation.second,
            )

            activities.unprocessedHandler != null ->
                activities.unprocessedHandler!!
                    .invokeCatching(
                        update = this,
                        parameters = params,
                        kind = TgInvocationKind.UNPROCESSED,
                        processingCtx = processingCtx,
                    )

            else -> logger.warn { "update: ${update.toJsonString()} not handled." }
        }
        middlewarePostInvokeShot(update, processingCtx)
    }

    private suspend fun InvocationLambda.invokeCatching(
        update: ProcessedUpdate,
        parameters: Map<String, String>,
        kind: TgInvocationKind,
        processingCtx: ProcessingCtx,
        meta: InvocationMeta? = null,
    ) {
        val user = update.userOrNull
        val target = when (kind) {
            TgInvocationKind.ACTIVITY -> "Method ${meta?.qualifier}:${meta?.function}"
            TgInvocationKind.TYPE -> "UpdateTypeHandler(${update.type})"
            TgInvocationKind.UNPROCESSED -> "UnprocessedHandler"
        }

        with(bot) {
            processingCtx.enrich(ProcessingCtxKey.INVOCATION_META, meta)
            processingCtx.enrich(ProcessingCtxKey.PARSED_PARAMETERS, parameters)
            processingCtx.enrich(ProcessingCtxKey.INVOCATION_KIND, kind)
        }
        runCatching {
            invoke(bot, update, parameters, processingCtx)
        }.onFailure {
            logger.error(it) {
                "Invocation error at update handling in $target with update: ${update.toJsonString()}"
            }
            handleFailure(update, it)
        }.onSuccess {
            logger.info { "Handled update#${update.updateId} to $target" }
        }
        if (meta != null && user != null && kind == TgInvocationKind.ACTIVITY) {
            userClassSteps[user.id] = meta.qualifier
        }
    }

    /**
     * Functional handling dsl
     *
     * @param update
     * @param block
     */
    suspend fun handle(update: ProcessedUpdate, block: FunctionalHandlingBlock) {
        logger.debug { "Functionally handling update: $update" }
        functionalHandlingBehavior.apply {
            block()
            process(update)
        }
    }

    companion object
}
