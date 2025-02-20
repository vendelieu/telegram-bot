package eu.vendeli.tgbot.interfaces.helper

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.component.ProcessedUpdate

/**
 * Interface which defines an exception handling process.
 *
 */
fun interface ExceptionHandler {
    /**
     * Handling action.
     *
     * @param exception Exception itself.
     * @param update Update that caused it.
     */
    suspend fun handle(exception: Throwable, update: ProcessedUpdate, bot: TelegramBot)
}
