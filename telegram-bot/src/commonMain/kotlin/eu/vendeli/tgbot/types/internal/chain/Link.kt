package eu.vendeli.tgbot.types.internal.chain

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.Action
import eu.vendeli.tgbot.types.internal.BreakCondition
import eu.vendeli.tgbot.types.internal.ProcessedUpdate

interface Link <T> {
    val retryAfterBreak: Boolean
    val breakCondition: BreakCondition?
    val beforeAction: Action?
    val afterAction: Action?

    suspend fun action(user: User, update: ProcessedUpdate, bot: TelegramBot): T

    suspend fun breakAction(user: User, update: ProcessedUpdate, bot: TelegramBot) {}
}
