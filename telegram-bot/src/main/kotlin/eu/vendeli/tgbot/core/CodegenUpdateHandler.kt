package eu.vendeli.tgbot.core

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.Update
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
import eu.vendeli.tgbot.utils.parseActivity
import eu.vendeli.tgbot.utils.processUpdate

/**
 * Processor for processing actions built via ksp code generation.
 *
 * @param actions generated actions.
 * @param bot bot instance.
 */
@Suppress("MagicNumber", "UNCHECKED_CAST")
class CodegenUpdateHandler internal constructor(
    actions: List<*>,
    bot: TelegramBot,
) : TgUpdateHandler(bot) {
    private val commandHandlers = actions[0] as CommandHandlers
    private val inputHandlers = actions[1] as InputHandlers
    private val regexHandlers = actions[2] as RegexHandlers
    private val updateTypeHandlers = actions[3] as UpdateTypeHandlers
    private val unprocessedHandler = actions[4] as InvocationLambda?

    override suspend fun handle(update: Update): Unit = update.processUpdate().run {
        logger.debug { "Handling update: $update" }
        // check general user limits
        if (checkIsLimited(bot.config.rateLimiter.limits, userOrNull?.id))
            return@run

        val request = parseActivity(text.substringBefore('@'))
        var activityId = request.command

        // check parsed command existence
        var invocation: Invocable? = commandHandlers[request.command to type]

        // if there's no command > check input point
        if (invocation == null) invocation = bot.inputListener.getAsync(userOrNull!!.id).await()?.let {
            activityId = it
            inputHandlers[it]
        }

        // remove input listener point
        if (userOrNull != null) bot.inputListener.del(userOrNull!!.id)

        // if there's no command and input > check regex handlers
        if (invocation == null) invocation = regexHandlers.entries.firstOrNull {
            it.key.matchEntire(text) != null
        }?.also {
            activityId = it.key.pattern
        }?.value

        logger.debug { "Result of finding action - ${invocation?.second}" }

        // if we found any action > check for its limits
        if (invocation != null && checkIsLimited(invocation.second.rateLimits, userOrNull?.id, activityId))
            return@run

        // invoke update type handler if there's
        updateTypeHandlers[type]?.invokeCatching(this, request.params, true)

        when {
            invocation != null -> invocation.invokeCatching(this, request.params)

            unprocessedHandler != null -> unprocessedHandler.invokeCatching(this, request.params)

            else -> logger.warn { "update: $update not handled." }
        }
    }

    private suspend fun Invocable.invokeCatching(pUpdate: ProcessedUpdate, params: Map<String, String>) {
        bot.chatData.run {
            if (pUpdate.userOrNull == null) return@run
            // check for user id nullability
            val prevClassName = getAsync<String>(pUpdate.userOrNull!!.id, "PrevInvokedClass").await()
            if (prevClassName != second.qualifier) clearAllAsync(pUpdate.userOrNull!!.id).await()

            setAsync(pUpdate.userOrNull!!.id, "PrevInvokedClass", second.function).await()
        }
        first.runCatching {
            invoke(bot.config.classManager, pUpdate, pUpdate.userOrNull, bot, params)
        }.onFailure {
            logger.error(
                it,
            ) { "Method ${second.qualifier} > ${second.function} invocation error at handling update: $pUpdate" }
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
