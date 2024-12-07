package eu.vendeli.tgbot.core

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.internal.ExperimentalFeature
import eu.vendeli.tgbot.annotations.internal.KtGramInternal
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.ActivitiesData
import eu.vendeli.tgbot.types.internal.FailedUpdate
import eu.vendeli.tgbot.types.internal.ProcessedUpdate
import eu.vendeli.tgbot.types.internal.UpdateType
import eu.vendeli.tgbot.types.internal.getOrNull
import eu.vendeli.tgbot.types.internal.userOrNull
import eu.vendeli.tgbot.utils.DEFAULT_HANDLING_BEHAVIOUR
import eu.vendeli.tgbot.utils.FunctionalHandlingBlock
import eu.vendeli.tgbot.utils.GET_UPDATES_ACTION
import eu.vendeli.tgbot.utils.HandlingBehaviourBlock
import eu.vendeli.tgbot.utils.Invocable
import eu.vendeli.tgbot.utils.InvocationLambda
import eu.vendeli.tgbot.utils.asyncAction
import eu.vendeli.tgbot.utils.checkIsGuarded
import eu.vendeli.tgbot.utils.checkIsLimited
import eu.vendeli.tgbot.utils.coHandle
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
import eu.vendeli.tgbot.utils.warn
import io.ktor.util.logging.trace
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

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

    internal val handlerScope = bot.config.updatesListener.run {
        CoroutineScope(SupervisorJob() + dispatcher + CoroutineName("TgBot"))
    }
    internal val functionalHandlingBehavior by lazy { FunctionalHandlingDsl(bot) }
    internal val logger = getLogger(bot.config.logging.botLogLevel, this::class.fqName)

    /**
     * The channel where errors caught during update processing is stored with update that caused them.
     */
    val caughtExceptions by lazy { Channel<FailedUpdate>(Channel.CONFLATED) }

    /**
     * Update flow being processed by the handler.
     */
    @ExperimentalFeature
    val flow: Flow<ProcessedUpdate> get() = updatesFlow

    /**
     * Previous invoked function qualified path (i.e., full class path).
     */
    @KtGramInternal
    val userClassSteps = mutableMapOf<Long, String>()

    private suspend fun collectUpdates(types: List<UpdateType>?) = bot.config.updatesListener.run {
        logger.debug { "Starting updates collector." }
        coHandle {
            var lastUpdateId = 0
            while (isActive) {
                logger.debug { "Running listener with offset - $lastUpdateId" }
                GET_UPDATES_ACTION
                    .options {
                        offset = lastUpdateId
                        allowedUpdates = types
                        timeout = updatesPollingTimeout
                    }.sendAsync(bot)
                    .getOrNull()
                    ?.forEach {
                        updatesFlow.emit(it)
                        lastUpdateId = it.updateId + 1
                    }
                pullingDelay.takeIf { it > 0 }?.let { delay(it) }
            }
        }
    }

    private suspend fun processUpdates() = bot.config.updatesListener.run {
        logger.info { "Starting long-polling listener." }
        coHandle {
            updatesFlow.collect { update ->
                launch(processingDispatcher) { handlingBehaviour(this@TgUpdateHandler, update) }
            }
        }.join()
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
        processUpdates()
    }

    /**
     * Stops listening of new updates.
     *
     */
    suspend fun stopListener() {
        handlerScope.coroutineContext.cancelChildren()
        logger.debug { "The listener is stopped." }
    }

    /**
     * A function for defining the behavior to handle updates.
     */
    suspend fun setBehaviour(block: HandlingBehaviourBlock) {
        logger.debug { "Handling behaviour is set." }
        stopListener()
        handlingBehaviour = block
    }

    /**
     * A method for handling updates from a string.
     * Define processing behavior before calling, see [setBehaviour].
     */
    suspend fun parseAndHandle(update: String) = parse(update)
        .await()
        ?.let {
            logger.debug { "Processing update with preset behaviour." }
            coHandle(
                bot.config.updatesListener.processingDispatcher,
            ) { handlingBehaviour(this@TgUpdateHandler, it) }
        }

    /**
     * A method to parse update from string.
     */
    suspend fun parse(update: String): Deferred<ProcessedUpdate?> = asyncAction {
        logger.debug { "Trying to parse update from string - $update" }
        return@asyncAction serde
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
        logger.debug { "Handling update: ${update.toJsonString()}" }
        logger.trace { "Processed into: $update" }
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

        logger.debug { "Result of parsing text: ${request.command} with parameters $params" }
        logger.debug { "Result of finding action - ${invocation?.second}" }

        // invoke update type handler if there's
        activities.updateTypeHandlers[type]?.invokeCatching(this, params, true)

        when {
            invocation != null -> invocation.invokeCatching(this, user, params)

            activities.unprocessedHandler != null ->
                activities.unprocessedHandler!!
                    .invokeCatching(this, params)

            else -> logger.warn { "update: $update not handled." }
        }
    }

    private suspend fun Invocable.invokeCatching(update: ProcessedUpdate, user: User?, params: Map<String, String>) {
        first
            .runCatching {
                invoke(bot.config.classManager, update, user, bot, params)
            }.onFailure {
                logger.error(
                    it,
                ) {
                    "Method ${second.qualifier}:${second.function} invocation error at handling update: ${update.toJsonString()}"
                }
                handleFailure(update, it)
            }.onSuccess {
                logger.info {
                    "Handled update#${update.updateId} to method ${second.qualifier + "::" + second.function}"
                }
            }
        user?.also { userClassSteps[it.id] = second.qualifier }
    }

    private suspend fun InvocationLambda.invokeCatching(
        update: ProcessedUpdate,
        params: Map<String, String>,
        isTypeUpdate: Boolean = false,
    ) = runCatching {
        invoke(bot.config.classManager, update, update.userOrNull, bot, params)
    }.onFailure {
        logger.error(it) {
            (if (isTypeUpdate) "UpdateTypeHandler(${update.type})" else "UnprocessedHandler") +
                " invocation error at handling update: ${update.toJsonString()}"
        }
        handleFailure(update, it)
    }.onSuccess {
        logger.info {
            "Handled update#${update.updateId} to " +
                if (isTypeUpdate) "UpdateTypeHandler(${update.type})" else "UnprocessedHandler"
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
