package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.configuration.RateLimits
import eu.vendeli.tgbot.utils.DEFAULT_COMMAND_SCOPE

interface InputLink {
    suspend fun action(
        update: ProcessedUpdate,
        user: User,
        bot: TelegramBot,
    )

    suspend fun breakCondition(
        update: ProcessedUpdate,
        user: User,
        bot: TelegramBot,
    ) = false
}

abstract class InputChain(private val nextLink: InputChain? = null) : InputLink {
    open val scope = DEFAULT_COMMAND_SCOPE
    open val rateLimits = RateLimits.NOT_LIMITED

    internal suspend fun link(
        update: ProcessedUpdate,
        user: User,
        bot: TelegramBot,
    ) {
        action(update, user, bot)
        if (breakCondition(update, user, bot)) return
        nextLink?.let { bot.inputListener[user] = it::class.java.canonicalName }
    }
}
