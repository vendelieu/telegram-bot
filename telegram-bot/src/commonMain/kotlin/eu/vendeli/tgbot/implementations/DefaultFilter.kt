package eu.vendeli.tgbot.implementations

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.interfaces.helper.Filter
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.ProcessedUpdate

object DefaultFilter : Filter {
    override suspend fun match(user: User?, update: ProcessedUpdate, bot: TelegramBot): Boolean = true
}
