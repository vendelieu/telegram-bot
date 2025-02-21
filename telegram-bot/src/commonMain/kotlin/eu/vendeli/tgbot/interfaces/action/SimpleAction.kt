package eu.vendeli.tgbot.interfaces.action

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.component.Response
import kotlinx.coroutines.Deferred

/**
 * Simple action, see [Actions article](https://github.com/vendelieu/telegram-bot/wiki/Actions)
 *
 * @param ReturnType response type.
 */

abstract class SimpleAction<ReturnType> : TgAction<ReturnType>() {
    /**
     * Send request for action.
     *
     * @param to recipient.
     */
    suspend fun send(to: TelegramBot) {
        doRequest(to)
    }

    /**
     * Send request returning its [Response].
     *
     * @param to recipient.
     */
    suspend inline fun sendReturning(to: TelegramBot): Deferred<Response<out ReturnType>> = doRequestReturning(to)
}
