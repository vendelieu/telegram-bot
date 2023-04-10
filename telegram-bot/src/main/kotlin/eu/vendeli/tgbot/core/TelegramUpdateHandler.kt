package eu.vendeli.tgbot.core

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.TelegramBot.Companion.mapper
import eu.vendeli.tgbot.interfaces.ClassManager
import eu.vendeli.tgbot.interfaces.InputListener
import eu.vendeli.tgbot.interfaces.RateLimitMechanism
import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.Actions
import eu.vendeli.tgbot.types.internal.Activity
import eu.vendeli.tgbot.types.internal.CallbackQueryUpdate
import eu.vendeli.tgbot.types.internal.Invocation
import eu.vendeli.tgbot.types.internal.MessageUpdate
import eu.vendeli.tgbot.types.internal.ProcessedUpdate
import eu.vendeli.tgbot.types.internal.UpdateType
import eu.vendeli.tgbot.types.internal.UserReference
import eu.vendeli.tgbot.utils.HandlingBehaviourBlock
import eu.vendeli.tgbot.utils.ManualHandlingBlock
import eu.vendeli.tgbot.utils.NewCoroutineContext
import eu.vendeli.tgbot.utils.checkIsLimited
import eu.vendeli.tgbot.utils.findAction
import eu.vendeli.tgbot.utils.invokeSuspend
import eu.vendeli.tgbot.utils.processUpdate
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mu.KotlinLogging
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
    internal val rateLimiter: RateLimitMechanism,
) {
    internal val logger = KotlinLogging.logger {}
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
                handlingBehaviour.runCatching {
                    invoke(this@TelegramUpdateHandler, update)
                }.onFailure { exception ->
                    logger.error(exception) { "Error at manually processing update: $update" }
                    caughtExceptions.send(exception to update)
                }
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
    @Suppress("CyclomaticComplexMethod", "SpreadOperator")
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
                if (parameters.keys.contains(parameterName)) when (p.parameterizedType.typeName) {
                    "java.lang.String" -> add(parameters[parameterName].toString())
                    "java.lang.Integer", "int" -> add(parameters[parameterName]?.toIntOrNull())
                    "java.lang.Long", "long" -> add(parameters[parameterName]?.toLongOrNull())
                    "java.lang.Short", "short" -> add(parameters[parameterName]?.toShortOrNull())
                    "java.lang.Float", "float" -> add(parameters[parameterName]?.toFloatOrNull())
                    "java.lang.Double", "double" -> add(parameters[parameterName]?.toDoubleOrNull())
                    else -> add(null)
                } else when {
                    typeName == "eu.vendeli.tgbot.types.User" -> add((pUpdate as? UserReference)?.user)
                    typeName == "eu.vendeli.tgbot.TelegramBot" -> add(bot)
                    typeName == "eu.vendeli.tgbot.types.internal.ProcessedUpdate" -> add(pUpdate)
                    bot.magicObjects.contains(p.type) -> add(bot.magicObjects[p.type]?.get(pUpdate, bot))
                    else -> add(null)
                }
            }
        }.toTypedArray()
        logger.debug { "Parsed arguments - $processedParameters." }

        bot.config.context._chatData?.run {
            if ((pUpdate as? UserReference)?.user?.id == null) return@run
            // check for user id nullability
            val prevClassName = getAsync<String>(pUpdate.user!!.id, "PrevInvokedClass").await()
            if (prevClassName != invocation.clazz.name) delPrevChatSectionAsync(pUpdate.user!!.id).await()

            setAsync(pUpdate.user!!.id, "PrevInvokedClass", invocation.clazz.name).await()
        }

        logger.debug { "Invoking function for Update#${pUpdate.updateId}" }
        invocation.runCatching {
            if (isSuspend) method.invokeSuspend(classManager.getInstance(clazz), *processedParameters)
            else method.invoke(classManager.getInstance(clazz), *processedParameters)
        }.onFailure {
            logger.error(it) { "Method {$invocation} invocation error at handling update: $pUpdate" }
            caughtExceptions.send((it.cause ?: it) to pUpdate.update)
        }.onSuccess { logger.info { "Handled update#${pUpdate.updateId} to method ${invocation.method}" } }
    }

    private suspend inline fun String?.getActivityOrNull(user: User?, updateType: UpdateType): Activity? {
        if (this == null) return null

        val commandAction = findAction(substringBefore('@'), updateType = updateType)
        var inputAction: Activity? = null

        if (user != null && commandAction == null) {
            inputAction = inputListener.getAsync(user.id).await()?.let {
                findAction(it, false, updateType)
            }
            inputListener.delAsync(user.id).await()
        }
        logger.debug { "Result of finding action - command: $commandAction, input: $inputAction" }

        return commandAction ?: inputAction
    }

    /**
     * Handle the update.
     *
     * @param update
     * @return null on success or [Throwable].
     */
    suspend fun handle(update: Update) = update.processUpdate().run {
        logger.debug { "Handling update: $update" }
        val user = if (this is UserReference) user else null
        val text = when (this) {
            is MessageUpdate -> message.text
            is CallbackQueryUpdate -> callbackQuery.data
            else -> null
        }
        if (checkIsLimited(bot.config.rateLimits, user?.id)) return@run null

        val action = text.getActivityOrNull(user, type)

        if (action != null && checkIsLimited(action.rateLimits, user?.id, action.id)) {
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
}
