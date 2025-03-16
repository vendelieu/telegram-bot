package eu.vendeli.tgbot.types.chain

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.component.ProcessedUpdate

interface Link<T> {
    val retryAfterBreak: Boolean
    val breakCondition: BreakCondition?
    val beforeAction: ChainAction?
    val afterAction: ChainAction?
    val chainingStrategy: ChainingStrategy
        get() = ChainingStrategy.Default

    suspend fun action(user: User, update: ProcessedUpdate, bot: TelegramBot): T

    suspend fun breakAction(user: User, update: ProcessedUpdate, bot: TelegramBot) {}
}
