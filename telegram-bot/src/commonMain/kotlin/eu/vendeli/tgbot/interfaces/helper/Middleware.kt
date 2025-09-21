package eu.vendeli.tgbot.interfaces.helper

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.component.ProcessedUpdate

/**
 * Represents a middleware interface for handling updates in a structured way.
 * Middleware defines a chain in which processing and actions are performed before and after
 * certain operations while interacting with a `TelegramBot` instance and `ProcessedUpdate`.
 */
abstract class Middleware {
    /**
     * Method to handle logic before processing an update.
     *
     * @param update The processed update that contains the information about the event.
     * @param bot The instance of the Telegram bot managing the update.
     */
    suspend fun preHandle(update: ProcessedUpdate, bot: TelegramBot) {}
    /**
     * Method invoked before the primary execution of a handler method.
     *
     * @param update The current update being processed, encapsulated within a [ProcessedUpdate] object.
     * @param bot The instance of [TelegramBot] associated with the current operation.
     */
    suspend fun preInvoke(update: ProcessedUpdate, bot: TelegramBot) {}
    /**
     * Invoked after the handling process is executed for an update.
     *
     * @param update Represents the processed update instance.
     * @param bot Instance of the Telegram bot processing the update.
     */
    suspend fun postInvoke(update: ProcessedUpdate, bot: TelegramBot) {}
}
