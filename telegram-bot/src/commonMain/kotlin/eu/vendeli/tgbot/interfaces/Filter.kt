package eu.vendeli.tgbot.interfaces

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.ProcessedUpdate

interface Filter {
    suspend fun condition(user: User?, update: ProcessedUpdate, bot: TelegramBot): Boolean
}
