package eu.vendeli.tgbot.core

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.interfaces.BotWaitingInput
import eu.vendeli.tgbot.interfaces.ClassManager
import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.internal.*
import eu.vendeli.tgbot.utils.CreateNewCoroutineContext
import eu.vendeli.tgbot.utils.invokeSuspend
import eu.vendeli.tgbot.utils.parseQuery
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

/**
 * A class that handles updates.
 *
 * @property actions The list of actions the handler will work with.
 * @property bot An instance of [TelegramBot]
 * @property classManager An instance of the class that will be used to call functions.
 * @property inputHandler An instance of the class that stores the input waiting points.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
class TelegramUpdateHandler internal constructor(
    private val actions: Actions,
    private val bot: TelegramBot,
    private val classManager: ClassManager,
    private val inputHandler: BotWaitingInput,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private lateinit var listener: suspend TelegramUpdateHandler.(Update) -> Unit
    private var handlerActive: Boolean = false

    /**
     * Function that starts the listening event.
     *
     * @param offset
     */
    private tailrec suspend fun runListener(offset: Int? = null): Int {
        logger.trace("Running listener with offset - $offset")
        if (!handlerActive) return 0
        var lastUpdateId: Int = offset ?: 0
        bot.pullUpdates(offset)?.forEach {
            CreateNewCoroutineContext(coroutineContext).launch(Dispatchers.IO) {
                listener(this@TelegramUpdateHandler, it)
            }
            lastUpdateId = it.updateId + 1
        }
        delay(100)
        return runListener(lastUpdateId)
    }

    /**
     * Function to define the actions that will be applied to updates when they are being processed.
     * When set, it starts an update processing cycle.
     *
     * @param block action that will be applied.
     * @receiver [CoroutineContext]
     */
    suspend fun setListener(block: suspend TelegramUpdateHandler.(Update) -> Unit) {
        logger.trace("The listener is set.")
        listener = block
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
        val message = text.parseQuery()
        val invocation = (
            if (command) actions.commands else {
                actions.inputs
            }
            )[message.command]
        return if (invocation != null) Activity(invocation = invocation, parameters = message.params) else null
    }

    /**
     * Function used to call functions with certain parameters processed after receiving update.
     *
     * @param update
     * @param invocation
     * @param parameters
     * @return null on success or [Throwable].
     */
    private suspend fun invokeMethod(
        update: ProcessedUpdate,
        invocation: Invocation,
        parameters: Map<String, String>,
    ): Throwable? {
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
        }

        bot.chatData?.run {
            logger.trace("Handling BotContext for Update#${update.fullUpdate.updateId}")
            if (!update.user.isPresent()) return@run
            val prevClassName = get(update.user.id, "PrevInvokedClass")?.toString()
            if (prevClassName != invocation.clazz.name) delPrevChatSection(update.user.id)

            set(update.user.id, "PrevInvokedClass", invocation.clazz.name)
        }

        logger.trace("Invoking function for Update#${update.fullUpdate.updateId}")
        invocation.runCatching {
            if (isSuspend) method.invokeSuspend(classManager.getInstance(clazz), *processedParameters.toTypedArray())
            else method.invoke(classManager.getInstance(clazz), *processedParameters.toTypedArray())
        }.onFailure {
            logger.debug("Method {$invocation} invocation error at handling update: $update", it)
            return it
        }.onSuccess { logger.debug("Handled update#${update.fullUpdate.updateId} to method $invocation") }
        return null
    }

    /**
     * Handle the update.
     *
     * @param update
     * @return null on success or [Throwable].
     */
    suspend fun handle(update: Update): Throwable? = processUpdateDto(update).run {
        logger.trace("Handling update: $update")
        val commandAction = if (text != null) findAction(text) else null
        val inputAction = if (commandAction == null) inputHandler.get(user.id)?.let { findAction(it, false) }
        else null
        logger.trace("Result of finding action - command: $commandAction, input: $inputAction")
        inputHandler.del(user.id)

        return when {
            commandAction != null -> invokeMethod(this, commandAction.invocation, commandAction.parameters)
            inputAction != null && update.message?.from?.isBot == false -> invokeMethod(
                this, inputAction.invocation, inputAction.parameters
            )
            actions.unhandled != null -> invokeMethod(this, actions.unhandled, emptyMap())
            else -> {
                logger.info("update: $update not handled.")
                null
            }
        }
    }

    /**
     * Manual handling dsl
     *
     * @param update
     * @param block
     */
    suspend fun handle(update: Update, block: suspend ManualHandlingDsl.(Update) -> Unit) {
        logger.trace("Manually handling update: $update")
        block(ManualHandlingDsl(inputHandler, update), update)
    }
}
