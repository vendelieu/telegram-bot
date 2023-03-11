package eu.vendeli.tgbot.interfaces

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.internal.Response
import eu.vendeli.tgbot.utils.makeRequestAsync
import eu.vendeli.tgbot.utils.makeSilentRequest
import kotlinx.coroutines.Deferred

/**
 * Simple action, see [Actions article](https://github.com/vendelieu/telegram-bot/wiki/Actions)
 *
 * @param ReturnType response type.
 */
interface SimpleAction<ReturnType> : TgAction<ReturnType>, IActionState {
    /**
     * Send request for action.
     *
     * @param to recipient.
     */
    suspend fun send(to: TelegramBot) {
        to.makeSilentRequest(method, parameters)
    }

    /**
     * Send async request for action.
     *
     * @param to recipient.
     */
    suspend fun sendAsync(
        to: TelegramBot,
    ): Deferred<Response<out ReturnType>> = to.makeRequestAsync(method, parameters, returnType, wrappedDataType)
}
