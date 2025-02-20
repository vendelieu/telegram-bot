package eu.vendeli.tgbot.interfaces.marker

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.component.ProcessedUpdate

/**
 * Interface for implementing the dynamic acquisition of a magic object
 *
 * @param T
 */
interface Autowiring<T : Any> {
    /**
     * method which will be used to retrieve the object.
     *
     * @param update
     * @param bot
     * @return [T]
     */
    suspend fun get(update: ProcessedUpdate, bot: TelegramBot): T?
}
