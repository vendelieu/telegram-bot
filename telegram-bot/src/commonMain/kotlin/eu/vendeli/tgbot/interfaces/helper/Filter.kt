package eu.vendeli.tgbot.interfaces.helper

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.ProcessedUpdate

interface Filter {
    suspend fun match(user: User?, update: ProcessedUpdate, bot: TelegramBot): Boolean
}
