package eu.vendeli.tgbot.core

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.interfaces.ClassManager
import eu.vendeli.tgbot.interfaces.InputListener
import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.internal.ProcessedUpdate
import eu.vendeli.tgbot.types.internal.UpdateType
import eu.vendeli.tgbot.types.internal.userOrNull
import eu.vendeli.tgbot.utils.Invocable
import eu.vendeli.tgbot.utils.InvocationLambda
import eu.vendeli.tgbot.utils.checkIsLimited
import eu.vendeli.tgbot.utils.parseCommand
import eu.vendeli.tgbot.utils.processUpdate

@Suppress("MagicNumber", "UNCHECKED_CAST")
class CodegenUpdateHandler(
    actions: List<*>,
    bot: TelegramBot,
    private val classManager: ClassManager,
    private val inputListener: InputListener,
) : TgUpdateHandler(bot, inputListener) {
    private val commandHandlers = actions[0] as Map<Pair<String, UpdateType>, Invocable>
    private val inputHandlers = actions[1] as Map<String, Invocable>
    private val regexHandlers = actions[2] as Map<Regex, Invocable>
    private val updateTypeHandlers = actions[3] as Map<UpdateType, InvocationLambda>
    private val unprocessedHandler = actions[4] as InvocationLambda?

    override suspend fun handle(update: Update): Unit = update.processUpdate().run {
        // check general user limits
        if (checkIsLimited(bot.config.rateLimiter.limits, userOrNull?.id))
            return@run

        val request = parseCommand(text.substringBefore('@'))
        var actionId = request.command

        // check parsed command existence
        var invocation: Invocable? = commandHandlers[request.command to type]

        // if there's no command > check input point
        if (invocation == null) invocation = inputListener.getAsync(userOrNull!!.id).await()?.let {
            actionId = it
            inputHandlers[it]
        }

        // remove input listener point
        if (userOrNull != null) inputListener.del(userOrNull!!.id)

        // if there's no command and input > check regex handlers
        if (invocation == null) invocation = regexHandlers.entries.firstOrNull {
            it.key.matchEntire(text) != null
        }?.also {
            actionId = it.key.pattern
        }?.value

        // if we found any action > check for its limits
        if (invocation != null && checkIsLimited(invocation.second.rateLimits, userOrNull?.id, actionId))
            return@run

        // invoke update type handler if there's
        updateTypeHandlers[type]?.invokeCatching(this, emptyMap(), true)

        when {
            invocation != null -> invocation.invokeCatching(this, request.params)

            unprocessedHandler != null -> unprocessedHandler.invokeCatching(this, emptyMap())

            else -> logger.warn { "update: $update not handled." }
        }
    }

    private suspend inline fun Invocable.invokeCatching(pUpdate: ProcessedUpdate, params: Map<String, String>) {
        bot.chatData.run {
            if (pUpdate.userOrNull == null) return@run
            // check for user id nullability
            val prevClassName = getAsync<String>(pUpdate.userOrNull!!.id, "PrevInvokedClass").await()
            if (prevClassName != second.qualifier) clearAllAsync(pUpdate.userOrNull!!.id).await()

            setAsync(pUpdate.userOrNull!!.id, "PrevInvokedClass", second.function).await()
        }
        first.runCatching {
            invoke(classManager, pUpdate, pUpdate.userOrNull, bot, params)
        }.onFailure {
            logger.error(
                it,
            ) { "Method ${second.qualifier} > ${second.function} invocation error at handling update: $pUpdate" }
            caughtExceptions.send((it.cause ?: it) to pUpdate.update)
        }.onSuccess {
            logger.info { "Handled update#${pUpdate.updateId} to method ${second.function}" }
        }
    }

    private suspend inline fun InvocationLambda.invokeCatching(
        pUpdate: ProcessedUpdate,
        params: Map<String, String>,
        isTypeUpdate: Boolean = true,
    ) = runCatching {
        invoke(classManager, pUpdate, pUpdate.userOrNull, bot, params)
    }.onFailure {
        logger.error(it) {
            (if (isTypeUpdate) "UpdateTypeHandler(${pUpdate.type})" else "UnprocessedHandler") +
                " invocation error at handling update: $pUpdate"
        }
        caughtExceptions.send((it.cause ?: it) to pUpdate.update)
    }.onSuccess {
        logger.info {
            "Handled update#${pUpdate.updateId} to " +
                if (isTypeUpdate) "UpdateTypeHandler(${pUpdate.type})" else "UnprocessedHandler"
        }
    }
}
