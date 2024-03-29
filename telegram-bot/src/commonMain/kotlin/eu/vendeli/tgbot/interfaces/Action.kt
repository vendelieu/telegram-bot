package eu.vendeli.tgbot.interfaces

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.internal.Response
import eu.vendeli.tgbot.utils.makeRequestAsync
import eu.vendeli.tgbot.utils.makeSilentRequest
import eu.vendeli.tgbot.utils.toJsonElement
import kotlinx.coroutines.Deferred

/**
 * Action, see [Actions article](https://github.com/vendelieu/telegram-bot/wiki/Actions)
 *
 * @param ReturnType response type.
 */
abstract class Action<ReturnType> : TgAction<ReturnType>() {
    /**
     * Make a request for action.
     *
     * @param to Recipient
     * @param via Instance of the bot through which the request will be made.
     */
    open suspend fun send(to: String, via: TelegramBot) {
        parameters["chat_id"] = to.toJsonElement()
        via.makeSilentRequest(method, parameters, multipartData)
    }

    open suspend fun send(to: Long, via: TelegramBot) {
        parameters["chat_id"] = to.toJsonElement()
        via.makeSilentRequest(method, parameters, multipartData)
    }

    open suspend fun send(to: User, via: TelegramBot) {
        parameters["chat_id"] = to.id.toJsonElement()
        via.makeSilentRequest(method, parameters, multipartData)
    }

    open suspend fun send(to: Chat, via: TelegramBot) {
        parameters["chat_id"] = to.id.toJsonElement()
        via.makeSilentRequest(method, parameters, multipartData)
    }

    /**
     * Make a request for action returning its [Response].
     *
     * @param to Recipient
     * @param via Instance of the bot through which the request will be made.
     */
    open suspend fun sendAsync(
        to: Long,
        via: TelegramBot,
    ): Deferred<Response<out ReturnType>> {
        parameters["chat_id"] = to.toJsonElement()
        return via.makeRequestAsync(method, parameters, returnType, multipartData)
    }

    open suspend fun sendAsync(
        to: String,
        via: TelegramBot,
    ): Deferred<Response<out ReturnType>> {
        parameters["chat_id"] = to.toJsonElement()
        return via.makeRequestAsync(method, parameters, returnType, multipartData)
    }

    open suspend fun sendAsync(
        to: User,
        via: TelegramBot,
    ): Deferred<Response<out ReturnType>> {
        parameters["chat_id"] = to.id.toJsonElement()
        return via.makeRequestAsync(method, parameters, returnType, multipartData)
    }

    open suspend fun sendAsync(
        to: Chat,
        via: TelegramBot,
    ): Deferred<Response<out ReturnType>> {
        parameters["chat_id"] = to.id.toJsonElement()
        return via.makeRequestAsync(method, parameters, returnType, multipartData)
    }
}
