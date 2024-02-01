package eu.vendeli.tgbot.types.internal

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.User

fun interface Action {
    suspend fun invoke(user: User, update: ProcessedUpdate, bot: TelegramBot)
}

fun interface BreakCondition {
    suspend fun invoke(user: User, update: ProcessedUpdate, bot: TelegramBot): Boolean
}

abstract class ChainLink {
    open val retryAfterBreak = true
    open val breakCondition: BreakCondition? = null

    abstract suspend fun action(user: User, update: ProcessedUpdate, bot: TelegramBot)

    open suspend fun breakAction(user: User, update: ProcessedUpdate, bot: TelegramBot) {}
}
