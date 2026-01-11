package eu.vendeli.tgbot.types.chain

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.component.ProcessedUpdate

fun interface ChainAction {
    suspend fun invoke(user: User, update: ProcessedUpdate, bot: TelegramBot)
}

fun interface BreakCondition {
    suspend fun invoke(user: User, update: ProcessedUpdate, bot: TelegramBot): Boolean
}

@Deprecated("Use Wizard instead, will be removed in 9.1")
abstract class ChainLink : Link<Unit> {
    override val retryAfterBreak = true
    override val breakCondition: BreakCondition? = null
    override val beforeAction: ChainAction? = null
    override val afterAction: ChainAction? = null
}
