package eu.vendeli.tgbot.interfaces.action

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.internal.InternalApi
import eu.vendeli.tgbot.types.internal.Response
import kotlinx.coroutines.Deferred

/**
 * Simple action, see [Actions article](https://github.com/vendelieu/telegram-bot/wiki/Actions)
 *
 * @param ReturnType response type.
 */
@OptIn(InternalApi::class)
abstract class SimpleAction<ReturnType> : TgAction<ReturnType>() {
    /**
     * Send request for action.
     *
     * @param to recipient.
     */
    open suspend fun send(to: TelegramBot) {
        doRequest(to)
    }

    /**
     * Send async request for action.
     *
     * @param to recipient.
     */
    open suspend fun sendAsync(
        to: TelegramBot,
    ): Deferred<Response<out ReturnType>> = doRequestReturning(to)

    suspend fun sendReturning(to: TelegramBot): Deferred<Response<out ReturnType>> = doRequestReturning(to)
}
