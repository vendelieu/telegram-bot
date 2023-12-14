package eu.vendeli.tgbot.core

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.TelegramBot.Companion.mapper
import eu.vendeli.tgbot.interfaces.ClassManager
import eu.vendeli.tgbot.interfaces.InputListener
import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.Actions
import eu.vendeli.tgbot.types.internal.Activity
import eu.vendeli.tgbot.types.internal.CallbackQueryUpdate
import eu.vendeli.tgbot.types.internal.Invocation
import eu.vendeli.tgbot.types.internal.MessageUpdate
import eu.vendeli.tgbot.types.internal.ProcessedUpdate
import eu.vendeli.tgbot.types.internal.UpdateType
import eu.vendeli.tgbot.types.internal.userOrNull
import eu.vendeli.tgbot.utils.HandlingBehaviourBlock
import eu.vendeli.tgbot.utils.ManualHandlingBlock
import eu.vendeli.tgbot.utils.NewCoroutineContext
import eu.vendeli.tgbot.utils.checkIsLimited
import eu.vendeli.tgbot.utils.findAction
import eu.vendeli.tgbot.utils.handleInvocation
import eu.vendeli.tgbot.utils.process
import eu.vendeli.tgbot.utils.processUpdate
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mu.KLogging
import kotlin.coroutines.coroutineContext

/**
 * A class that handles updates.
 *
 * @property actions The list of actions the handler will work with.
 * @property bot An instance of [TelegramBot]
 * @property classManager An instance of the class that will be used to call functions.
 * @property inputListener An instance of the class that stores the input waiting points.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
class TelegramUpdateHandler internal constructor(
    internal val actions: Actions? = null,
    internal val bot: TelegramBot,
    private val classManager: ClassManager,
    private val inputListener: InputListener,
) {
    private lateinit var handlingBehaviour: HandlingBehaviourBlock

    @Volatile
    private var handlerActive: Boolean = false
    private val manualHandlingBehavior by lazy { ManualHandlingDsl(bot, inputListener) }
    val caughtExceptions by lazy { Channel<Pair<Throwable, Update>>(CONFLATED) }

    /**
     * Function that starts the listening event.
     *
     * @param offset
     */
    private tailrec suspend fun runListener(offset: Int? = null): Int = with(bot.config.updatesListener) {
        logger.debug { "Running listener with offset - $offset" }
        if (!handlerActive) {
            coroutineContext.cancelChildren()
            return 0
        }
        var lastUpdateId: Int = offset ?: 0
        bot.pullUpdates(offset)?.forEach { update ->
            NewCoroutineContext(coroutineContext + dispatcher).launch {
                handlingBehaviour.invoke(this@TelegramUpdateHandler, update)
            }
            lastUpdateId = update.updateId + 1
        }
        delay(pullingDelay)
        return runListener(lastUpdateId)
    }

    /**
     * Function to define the actions that will be applied to updates when they are being processed.
     * When set, it starts an update processing cycle.
     *
     * @param block action that will be applied.
     */
    suspend fun setListener(block: HandlingBehaviourBlock) {
        if (handlerActive) stopListener()
        logger.debug { "The listener is set." }
        handlingBehaviour = block
        handlerActive = true
        runListener()
    }

    /**
     * Stops listening of new updates.
     *
     */
    fun stopListener() {
        logger.debug { "The listener is stopped." }
        handlerActive = false
    }

    /**
     * A function for defining the behavior to handle updates.
     */
    fun setBehaviour(block: HandlingBehaviourBlock) {
        logger.debug { "Handling behaviour is set." }
        handlingBehaviour = block
    }

    /**
     * A method for handling updates from a string.
     * Define processing behaviour before calling, see [setBehaviour].
     */
    suspend fun parseAndHandle(update: String) {
        logger.debug { "Trying to parse update from string - $update" }
        mapper.runCatching {
            readValue(update, Update::class.java)
        }.onFailure {
            logger.debug(it) { "error during the update parsing process." }
        }.onSuccess { logger.info { "Successfully parsed update to $it" } }
            .getOrNull()?.let { handlingBehaviour(this, it) }
    }

    /**
     * Function used to call functions with certain parameters processed after receiving update.
     *
     * @param pUpdate
     * @param invocation
     * @param parameters
     * @return null on success or [Throwable].
     */
    @Suppress("CyclomaticComplexMethod")
    private suspend fun invokeMethod(
        pUpdate: ProcessedUpdate,
        invocation: Invocation,
        parameters: Map<String, String>,
    ) {
        var isSuspend = false
        logger.debug { "Parsing arguments for Update#${pUpdate.updateId}" }
        val processedParameters = buildList {
            invocation.method.parameters.forEach { p ->
                if (p.type.name == "kotlin.coroutines.Continuation") {
                    isSuspend = true
                    return@forEach
                }
                val parameterName = invocation.namedParameters.getOrDefault(p.name, p.name)
                val typeName = p.parameterizedType.typeName
                if (parameterName in parameters.keys) when (p.parameterizedType.typeName) {
                    "java.lang.String" -> add(parameters[parameterName].toString())
                    "java.lang.Integer", "int" -> add(parameters[parameterName]?.toIntOrNull())
                    "java.lang.Long", "long" -> add(parameters[parameterName]?.toLongOrNull())
                    "java.lang.Short", "short" -> add(parameters[parameterName]?.toShortOrNull())
                    "java.lang.Float", "float" -> add(parameters[parameterName]?.toFloatOrNull())
                    "java.lang.Double", "double" -> add(parameters[parameterName]?.toDoubleOrNull())
                    else -> add(null)
                } else when {
                    typeName == User::class.java.canonicalName -> add(pUpdate.userOrNull)
                    typeName == TelegramBot::class.java.canonicalName -> add(bot)
                    typeName == ProcessedUpdate::class.java.canonicalName -> add(pUpdate)
                    typeName == MessageUpdate::class.java.canonicalName -> add(pUpdate)
                    typeName == CallbackQueryUpdate::class.java.canonicalName -> add(pUpdate)
                    p.type in bot.autowiringObjects -> add(bot.autowiringObjects[p.type]?.get(pUpdate, bot))
                    else -> add(null)
                }
            }
        }.also { logger.debug { "Parsed arguments - $it." } }.toTypedArray()

        bot.config.context._chatData?.run {
            if (pUpdate.userOrNull == null) return@run
            // check for user id nullability
            val prevClassName = getAsync<String>(pUpdate.userOrNull!!.id, "PrevInvokedClass").await()
            if (prevClassName != invocation.clazz.name) clearAllAsync(pUpdate.userOrNull!!.id).await()

            setAsync(pUpdate.userOrNull!!.id, "PrevInvokedClass", invocation.clazz.name).await()
        }

        logger.debug { "Invoking function for Update#${pUpdate.updateId}" }
        invocation.runCatching {
            method.handleInvocation(clazz, classManager, processedParameters, isSuspend)
        }.onFailure {
            logger.error(it) { "Method $invocation invocation error at handling update: $pUpdate" }
            caughtExceptions.send((it.cause ?: it) to pUpdate.update)
        }.onSuccess {
            logger.info { "Handled update#${pUpdate.updateId} to ${invocation.type} method ${invocation.method}" }
        }
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun String.getActivityOrNull(user: User?, updateType: UpdateType): Activity? {
        var activity = findAction(substringBefore('@'), updateType = updateType)

        if (user != null && activity == null) {
            activity = inputListener.get(user.id)?.let {
                findAction(it, false, updateType)
            }
        }
        if (user != null) inputListener.del(user.id)
        logger.debug { "Result of finding action - ${activity?.invocation?.type ?: ""} $activity" }

        return activity
    }

    /**
     * Handle the update.
     *
     * @param update
     * @return null on success or [Throwable].
     */
    suspend fun handle(update: Update) = update.processUpdate().run {
        logger.debug { "Handling update: $update" }
        if (checkIsLimited(bot.config.rateLimiter.limits, userOrNull?.id)) return@run null

        val action = text.getActivityOrNull(userOrNull, type)

        if (action != null && checkIsLimited(action.rateLimits, userOrNull?.id, action.id)) {
            return@run null
        }

        actions?.updateHandlers?.get(type)?.also { invokeMethod(this, it, emptyMap()) }

        when {
            action != null -> invokeMethod(this, action.invocation, action.parameters)

            actions?.unhandled != null -> invokeMethod(this, actions.unhandled, emptyMap())

            else -> logger.warn { "update: $update not handled." }
        }
    }

    /**
     * Manual handling dsl
     *
     * @param update
     * @param block
     */
    suspend fun handle(update: Update, block: ManualHandlingBlock) {
        logger.debug { "Manually handling update: $update" }
        manualHandlingBehavior.apply {
            block()
            process(update)
        }
    }

    internal companion object : KLogging()
}
