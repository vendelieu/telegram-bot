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
abstract class SimpleAction<ReturnType> : TgAction<ReturnType>() {
    /**
     * Send request for action.
     *
     * @param to recipient.
     */
    open suspend fun send(to: TelegramBot) {
        to.makeSilentRequest(method, parameters)
    }

    /**
     * Send async request for action.
     *
     * @param to recipient.
     */
    open suspend fun sendAsync(
        to: TelegramBot,
    ): Deferred<Response<out ReturnType>> = to.makeRequestAsync(method, parameters, returnType, collectionReturnType)
}
