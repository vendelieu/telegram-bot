package eu.vendeli.tgbot.core

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.ActivitiesData
import eu.vendeli.tgbot.types.internal.FailedUpdate
import eu.vendeli.tgbot.types.internal.ProcessedUpdate
import eu.vendeli.tgbot.types.internal.userOrNull
import eu.vendeli.tgbot.utils.Invocable
import eu.vendeli.tgbot.utils.InvocationLambda
import eu.vendeli.tgbot.utils.checkIsGuarded
import eu.vendeli.tgbot.utils.checkIsLimited
import eu.vendeli.tgbot.utils.parseCommand

/**
 * Processor for processing actions built via ksp code generation.
 *
 * @param activities generated actions.
 * @param bot bot instance.
 */
class CodegenUpdateHandler internal constructor(
    commandsPackage: String? = null,
    bot: TelegramBot,
) : TgUpdateHandler(bot) {
    private val activities by lazy { ActivitiesData(commandsPackage) }

    override suspend fun handle(update: ProcessedUpdate): Unit = update.run {
        logger.debug { "Handling update: $update" }
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

        logger.debug { "Result of finding action - ${invocation?.second}" }

        // check guard condition
        if (invocation?.second?.guard?.checkIsGuarded(user, this, bot) == false) {
            logger.debug { "Invocation guarded: ${invocation.second}" }
            return@run
        }

        // if we found any action > check for its limits
        if (invocation != null && checkIsLimited(invocation.second.rateLimits, user?.id, activityId))
            return@run

        // invoke update type handler if there's
        activities.updateTypeHandlers[type]?.invokeCatching(this, request.params, true)

        when {
            invocation != null -> invocation.invokeCatching(this, user, request.params)

            activities.unprocessedHandler != null ->
                activities.unprocessedHandler!!
                    .invokeCatching(this, request.params)

            else -> logger.warn { "update: $update not handled." }
        }
    }

    private suspend fun Invocable.invokeCatching(update: ProcessedUpdate, user: User?, params: Map<String, String>) {
        first.runCatching {
            invoke(bot.config.classManager, update, user, bot, params)
        }.onFailure {
            logger.error(
                it,
            ) { "Method ${second.qualifier}:${second.function} invocation error at handling update: $update" }
            caughtExceptions.send(FailedUpdate(it, update))
        }.onSuccess {
            logger.info {
                "Handled update#${update.updateId} to method ${second.qualifier + "::" + second.function}"
            }
        }
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
                " invocation error at handling update: $update"
        }
        caughtExceptions.send(FailedUpdate(it, update))
    }.onSuccess {
        logger.info {
            "Handled update#${update.updateId} to " +
                if (isTypeUpdate) "UpdateTypeHandler(${update.type})" else "UnprocessedHandler"
        }
    }
}
