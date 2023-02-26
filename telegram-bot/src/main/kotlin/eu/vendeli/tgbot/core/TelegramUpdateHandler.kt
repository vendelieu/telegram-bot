package eu.vendeli.tgbot.core

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.TelegramBot.Companion.mapper
import eu.vendeli.tgbot.interfaces.BotInputListener
import eu.vendeli.tgbot.interfaces.ClassManager
import eu.vendeli.tgbot.interfaces.RateLimitMechanism
import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.internal.Actions
import eu.vendeli.tgbot.types.internal.Activity
import eu.vendeli.tgbot.types.internal.Invocation
import eu.vendeli.tgbot.types.internal.ProcessedUpdate
import eu.vendeli.tgbot.utils.HandlingBehaviourBlock
import eu.vendeli.tgbot.utils.ManualHandlingBlock
import eu.vendeli.tgbot.utils.NewCoroutineContext
import eu.vendeli.tgbot.utils.checkIsLimited
import eu.vendeli.tgbot.utils.invokeSuspend
import eu.vendeli.tgbot.utils.parseCommand
import eu.vendeli.tgbot.utils.processUpdate
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
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
    private val actions: Actions? = null,
    internal val bot: TelegramBot,
    private val classManager: ClassManager,
    private val inputListener: BotInputListener,
    internal val rateLimiter: RateLimitMechanism,
) {
    internal val logger = LoggerFactory.getLogger(javaClass)
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
        logger.trace("Running listener with offset - $offset")
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
                    logger.error("Error at manually processing update: $update", exception)
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
        logger.trace("The listener is set.")
        handlingBehaviour = block
        handlerActive = true
        runListener()
    }

    /**
     * Stops listening of new updates.
     *
     */
    fun stopListener() {
        logger.trace("The listener is stopped.")
        handlerActive = false
    }

    /**
     * Function for mapping text with a specific command or input.
     *
     * @param text
     * @param command true to search in commands or false to search among inputs. Default - true.
     * @return [Activity] if actions was found or null.
     */
    private fun findAction(text: String, command: Boolean = true): Activity? {
        val message = parseCommand(text)
        val invocation = if (command) actions?.commands else {
            actions?.inputs
        }?.get(message.command)
        return if (invocation != null) Activity(
            id = message.command,
            invocation = invocation,
            parameters = message.params,
            rateLimits = invocation.rateLimits,
        ) else null
    }

    /**
     * Updates parsing method
     *
     * @param update
     * @return [Update] or null
     */
    private fun parseUpdate(update: String): Update? {
        logger.trace("Trying to parse update from string - $update")
        return mapper.runCatching {
            readValue(update, Update::class.java)
        }.onFailure {
            logger.debug("error during the update parsing process.", it)
        }.onSuccess { logger.trace("Successfully parsed update to $it") }.getOrNull()
    }

    /**
     * A function for defining the behavior to handle updates.
     */
    fun setBehaviour(block: HandlingBehaviourBlock) {
        logger.trace("Handling behaviour is set.")
        handlingBehaviour = block
    }

    /**
     * A method for handling updates from a string.
     * Define processing behaviour before calling, see [setBehaviour].
     */
    suspend fun parseAndHandle(update: String) =
        parseUpdate(update)?.let { handlingBehaviour(this, it) }

    /**
     * Function used to call functions with certain parameters processed after receiving update.
     *
     * @param update
     * @param invocation
     * @param parameters
     * @return null on success or [Throwable].
     */
    @Suppress("CyclomaticComplexMethod", "SpreadOperator")
    private suspend fun invokeMethod(
        update: ProcessedUpdate,
        invocation: Invocation,
        parameters: Map<String, String>,
    ) {
        var isSuspend = false
        logger.trace("Parsing arguments for Update#${update.fullUpdate.updateId}")
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
                    typeName == "eu.vendeli.tgbot.types.User" -> add(update.user)
                    typeName == "eu.vendeli.tgbot.TelegramBot" -> add(bot)
                    typeName == "eu.vendeli.tgbot.types.internal.ProcessedUpdate" -> add(update)
                    bot.magicObjects.contains(p.type) -> add(bot.magicObjects[p.type]?.get(update, bot))
                    else -> add(null)
                }
            }
        }.toTypedArray()

        bot.config.context._chatData?.run {
            if (!update.user.isPresent()) return@run
            logger.trace("Handling BotContext for Update#${update.fullUpdate.updateId}")
            val prevClassName = getAsync<String>(update.user.id, "PrevInvokedClass").await()
            if (prevClassName != invocation.clazz.name) delPrevChatSectionAsync(update.user.id).await()

            setAsync(update.user.id, "PrevInvokedClass", invocation.clazz.name).await()
        }

        logger.trace("Invoking function for Update#${update.fullUpdate.updateId}")
        invocation.runCatching {
            if (isSuspend) method.invokeSuspend(classManager.getInstance(clazz), *processedParameters)
            else method.invoke(classManager.getInstance(clazz), *processedParameters)
        }.onFailure {
            logger.error("Method {$invocation} invocation error at handling update: $update", it)
            caughtExceptions.send((it.cause ?: it) to update.fullUpdate)
        }.onSuccess { logger.debug("Handled update#${update.fullUpdate.updateId} to method $invocation") }
    }

    /**
     * Handle the update.
     *
     * @param update
     * @return null on success or [Throwable].
     */
    @Suppress("CyclomaticComplexMethod")
    suspend fun handle(update: Update) = processUpdate(update).run {
        logger.trace("Handling update: $update")
        val telegramId = update.message?.from?.id
        if (checkIsLimited(bot.config.rateLimits, telegramId)) return@run null

        val commandAction = if (text != null) findAction(text.substringBefore('@')) else null
        val inputAction = if (commandAction == null) inputListener.getAsync(user.id).await()?.let {
            findAction(it, false)
        } else null
        logger.trace("Result of finding action - command: $commandAction, input: $inputAction")
        inputListener.delAsync(user.id).await()

        val action = commandAction ?: inputAction
        if (action != null && checkIsLimited(action.rateLimits, telegramId, action.id)) {
            return@run null
        }

        actions?.updateHandlers?.get(type)?.also { invokeMethod(this, it, emptyMap()) }

        when {
            commandAction != null -> invokeMethod(this, commandAction.invocation, commandAction.parameters)

            inputAction != null && update.message?.from?.isBot == false -> {
                invokeMethod(this, inputAction.invocation, inputAction.parameters)
            }

            actions?.unhandled != null -> invokeMethod(this, actions.unhandled, emptyMap())

            else -> logger.info("update: $update not handled.")
        }
    }

    /**
     * Manual handling dsl
     *
     * @param update
     * @param block
     */
    suspend fun handle(update: Update, block: ManualHandlingBlock) {
        logger.trace("Manually handling update: $update")
        manualHandlingBehavior.apply {
            block()
            process(update)
        }
    }
}
