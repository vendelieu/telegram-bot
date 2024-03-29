package eu.vendeli.tgbot.interfaces

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.internal.ProcessedUpdate

/**
 * Interface for implementing the dynamic acquisition of a magic object
 *
 * @param T
 */
fun interface Autowiring<T> {
    /**
     * method which will be used to retrieve the object.
     *
     * @param update
     * @param bot
     * @return [T]
     */
    suspend fun get(update: ProcessedUpdate, bot: TelegramBot): T?
}
