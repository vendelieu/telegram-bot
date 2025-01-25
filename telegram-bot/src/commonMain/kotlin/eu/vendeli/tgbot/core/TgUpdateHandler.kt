package eu.vendeli.tgbot.core

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.internal.ExperimentalFeature
import eu.vendeli.tgbot.annotations.internal.KtGramInternal
import eu.vendeli.tgbot.types.internal.ActivitiesData
import eu.vendeli.tgbot.types.internal.FailedUpdate
import eu.vendeli.tgbot.types.internal.InvocationMeta
import eu.vendeli.tgbot.types.internal.ProcessedUpdate
import eu.vendeli.tgbot.types.internal.TgInvocationKind
import eu.vendeli.tgbot.types.internal.UpdateType
import eu.vendeli.tgbot.types.internal.getOrNull
import eu.vendeli.tgbot.types.internal.userOrNull
import eu.vendeli.tgbot.utils.DEFAULT_HANDLING_BEHAVIOUR
import eu.vendeli.tgbot.utils.FunctionalHandlingBlock
import eu.vendeli.tgbot.utils.GET_UPDATES_ACTION
import eu.vendeli.tgbot.utils.HandlingBehaviourBlock
import eu.vendeli.tgbot.utils.Invocable
import eu.vendeli.tgbot.utils.InvocationLambda
import eu.vendeli.tgbot.utils.TgException
import eu.vendeli.tgbot.utils.checkIsGuarded
import eu.vendeli.tgbot.utils.checkIsLimited
import eu.vendeli.tgbot.utils.debug
import eu.vendeli.tgbot.utils.error
import eu.vendeli.tgbot.utils.fqName
import eu.vendeli.tgbot.utils.getLogger
import eu.vendeli.tgbot.utils.getParameters
import eu.vendeli.tgbot.utils.handleFailure
import eu.vendeli.tgbot.utils.info
import eu.vendeli.tgbot.utils.parseCommand
import eu.vendeli.tgbot.utils.process
import eu.vendeli.tgbot.utils.serde
import eu.vendeli.tgbot.utils.toJsonElement
import eu.vendeli.tgbot.utils.warn
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.util.logging.trace
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    private suspend fun collectUpdates(types: List<UpdateType>?) = coroutineScope {
        val cfg = bot.config.updatesListener
        logger.debug { "Starting updates collector." }

        withContext(Job(handlerJob)) {
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
                    throw TgException("Connection timeout", e)
                }
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
        logger.trace { "Handling update: ${update.toJsonString()}\nProcessed into: $update" }
        val user = userOrNull
        // check general user limits
        if (checkIsLimited(bot.config.rateLimiter.limits, user?.id))
            return@run

        val request = parseCommand(text)
        var activityId = request.command

        // check parsed command existence
        var invocation: Invocable? = activities.commandHandlers[request.command to type]

        // if there's no command > check input point
        if (invocation == null && user != null)
            invocation = bot.inputListener.getAsync(user.id).await()?.let {
                activityId = it
                activities.inputHandlers[it]
            }

        // remove input listener point
        if (user != null && bot.config.inputAutoRemoval) bot.inputListener.del(user.id)

        // if there's no command and input > check common handlers
        if (invocation == null) invocation = activities.commonHandlers.entries
            .firstOrNull {
                it.key.match(request.command, this, bot)
            }?.also {
                activityId = it.key.value.toString()
            }?.value

        // check guard condition
        if (invocation?.second?.guard?.checkIsGuarded(user, this, bot) == false) {
            logger.debug { "Invocation guarded: ${invocation.second}" }
            return@run
        }

        // if we found any action > check for its limits
        if (invocation != null && checkIsLimited(invocation.second.rateLimits, user?.id, activityId))
            return@run

        val params = getParameters(invocation?.second?.argParser, request)

        logger.debug {
            "Result of parsing text: ${request.command} with parameters $params" +
                "\nResult of finding action - ${invocation?.second}"
        }

        // invoke update type handler if there's
        activities.updateTypeHandlers[type]?.invokeCatching(this, params, TgInvocationKind.TYPE)

        when {
            invocation != null -> invocation.first.invokeCatching(
                this,
                params,
                TgInvocationKind.ACTIVITY,
                invocation.second,
            )

            activities.unprocessedHandler != null ->
                activities.unprocessedHandler!!
                    .invokeCatching(this, params, TgInvocationKind.UNPROCESSED)

            else -> logger.warn { "update: $update not handled." }
        }
    }

    private suspend fun InvocationLambda.invokeCatching(
        update: ProcessedUpdate,
        parameters: Map<String, String>,
        kind: TgInvocationKind,
        meta: InvocationMeta? = null,
    ) {
        val user = update.userOrNull
        val target = when (kind) {
            TgInvocationKind.ACTIVITY -> "Method ${meta?.qualifier}:${meta?.function}"
            TgInvocationKind.TYPE -> "UpdateTypeHandler(${update.type})"
            TgInvocationKind.UNPROCESSED -> "UnprocessedHandler"
        }

        runCatching {
            invoke(bot.config.classManager, update, user, bot, parameters)
        }.onFailure {
            logger.error(it) {
                "Invocation error at update handling in $target with update: ${update.toJsonString()}"
            }
            handleFailure(update, it)
        }.onSuccess {
            logger.info { "Handled update#${update.updateId} to $target" }
        }
        if (meta != null && user != null) {
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
