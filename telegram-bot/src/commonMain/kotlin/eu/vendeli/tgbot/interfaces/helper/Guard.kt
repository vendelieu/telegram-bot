package eu.vendeli.tgbot.interfaces.helper

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.ProcessedUpdate

/**
 * Helper interface used with guard mechanics for several handlers.
 */
fun interface Guard {
    suspend fun condition(user: User?, update: ProcessedUpdate, bot: TelegramBot): Boolean
}
