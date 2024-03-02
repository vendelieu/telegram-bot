package eu.vendeli.tgbot.core

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.Update
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.FailedUpdate
import eu.vendeli.tgbot.types.internal.ProcessedUpdate
import eu.vendeli.tgbot.types.internal.userOrNull
import eu.vendeli.tgbot.utils.CommandHandlers
import eu.vendeli.tgbot.utils.InputHandlers
import eu.vendeli.tgbot.utils.Invocable
import eu.vendeli.tgbot.utils.InvocationLambda
import eu.vendeli.tgbot.utils.RegexHandlers
import eu.vendeli.tgbot.utils.UpdateTypeHandlers
import eu.vendeli.tgbot.utils.checkIsLimited
import eu.vendeli.tgbot.utils.logString
import eu.vendeli.tgbot.utils.parseCommand
import eu.vendeli.tgbot.utils.processUpdate

/**
 * Processor for processing actions built via ksp code generation.
 *
 * @param activities generated actions.
 * @param bot bot instance.
 */
@Suppress("MagicNumber", "UNCHECKED_CAST")
class CodegenUpdateHandler internal constructor(
    activities: List<*>,
    bot: TelegramBot,
) : TgUpdateHandler(bot) {
    private val commandHandlers = activities[0] as CommandHandlers
    private val inputHandlers = activities[1] as InputHandlers
    private val regexHandlers = activities[2] as RegexHandlers
    private val updateTypeHandlers = activities[3] as UpdateTypeHandlers
    private val unprocessedHandler = activities[4] as InvocationLambda?

    init {
        logger.info {
            "\nCommandHandlers:\n${commandHandlers.logString}\n" +
                "InputHandlers:\n${inputHandlers.logString}\n" +
                "RegexCommandHandlers:\n${regexHandlers.logString}\n" +
                "UpdateTypeHandlers:\n${updateTypeHandlers.logString}\n" +
                "UnprocessedHandler:\n${unprocessedHandler ?: "None"}"
        }
    }

    override suspend fun handle(update: Update): Unit = update.processUpdate().run {
        logger.debug { "Handling update: $update" }
        val user = userOrNull
        // check general user limits
        if (checkIsLimited(bot.config.rateLimiter.limits, user?.id))
            return@run

        val request = parseCommand(text.substringBefore('@'))
        var activityId = request.command

        // check parsed command existence
        var invocation: Invocable? = commandHandlers[request.command to type]

        // if there's no command > check input point
        if (invocation == null && user != null)
            invocation = bot.inputListener.getAsync(user.id).await()?.let {
                activityId = it
                inputHandlers[it]
            }

        // remove input listener point
        if (user != null) bot.inputListener.del(user.id)

        // if there's no command and input > check regex handlers
        if (invocation == null) invocation = regexHandlers.entries.firstOrNull {
            it.key.matchEntire(text) != null
        }?.also {
            activityId = it.key.pattern
        }?.value

        logger.debug { "Result of finding action - ${invocation?.second}" }

        // if we found any action > check for its limits
        if (invocation != null && checkIsLimited(invocation.second.rateLimits, user?.id, activityId))
            return@run

        // invoke update type handler if there's
        updateTypeHandlers[type]?.invokeCatching(this, request.params, true)

        when {
            invocation != null -> invocation.invokeCatching(this, user, request.params)

            unprocessedHandler != null -> unprocessedHandler.invokeCatching(this, request.params)

            else -> logger.warn { "update: $update not handled." }
        }
    }

    private suspend fun Invocable.invokeCatching(pUpdate: ProcessedUpdate, user: User?, params: Map<String, String>) {
        bot.chatData.run {
            if (user == null) return@run
            // check for user id nullability
            val prevClassName = getAsync<String>(user.id, "PrevInvokedClass").await()
            if (prevClassName != second.qualifier) clearAllAsync(user.id).await()

            setAsync(user.id, "PrevInvokedClass", second.function).await()
        }
        first.runCatching {
            invoke(bot.config.classManager, pUpdate, user, bot, params)
        }.onFailure {
            logger.error(
                it,
            ) { "Method ${second.qualifier}:${second.function} invocation error at handling update: $pUpdate" }
            caughtExceptions.send(FailedUpdate(it.cause ?: it, pUpdate.update))
        }.onSuccess {
            logger.info { "Handled update#${pUpdate.updateId} to method ${second.qualifier + "::" + second.function}" }
        }
    }

    private suspend fun InvocationLambda.invokeCatching(
        pUpdate: ProcessedUpdate,
        params: Map<String, String>,
        isTypeUpdate: Boolean = false,
    ) = runCatching {
        invoke(bot.config.classManager, pUpdate, pUpdate.userOrNull, bot, params)
    }.onFailure {
        logger.error(it) {
            (if (isTypeUpdate) "UpdateTypeHandler(${pUpdate.type})" else "UnprocessedHandler") +
                " invocation error at handling update: $pUpdate"
        }
        caughtExceptions.send(FailedUpdate(it.cause ?: it, pUpdate.update))
    }.onSuccess {
        logger.info {
            "Handled update#${pUpdate.updateId} to " +
                if (isTypeUpdate) "UpdateTypeHandler(${pUpdate.type})" else "UnprocessedHandler"
        }
    }
}
