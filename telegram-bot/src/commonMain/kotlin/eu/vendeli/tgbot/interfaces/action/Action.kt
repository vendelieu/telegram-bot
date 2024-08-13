package eu.vendeli.tgbot.interfaces.action

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.internal.InternalApi
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.internal.Response
import eu.vendeli.tgbot.utils.toJsonElement
import kotlinx.coroutines.Deferred

/**
 * Action, see [Actions article](https://github.com/vendelieu/telegram-bot/wiki/Actions)
 *
 * @param ReturnType response type.
 */
@OptIn(InternalApi::class)
abstract class Action<ReturnType> : TgAction<ReturnType>() {
    /**
     * Make a request for action.
     *
     * @param to Recipient
     * @param via Instance of the bot through which the request will be made.
     */
    open suspend fun send(to: String, via: TelegramBot) {
        parameters["chat_id"] = to.toJsonElement()
        doRequest(via)
    }

    open suspend fun send(to: Long, via: TelegramBot) {
        parameters["chat_id"] = to.toJsonElement()
        doRequest(via)
    }

    open suspend fun send(to: User, via: TelegramBot) {
        parameters["chat_id"] = to.id.toJsonElement()
        doRequest(via)
    }

    open suspend fun send(to: Chat, via: TelegramBot) {
        parameters["chat_id"] = to.id.toJsonElement()
        doRequest(via)
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
        return doRequestReturning(via)
    }

    /**
     * Make a request for action returning its [Response].
     *
     * @param to Recipient
     * @param via Instance of the bot through which the request will be made.
     */
    open suspend fun sendReturning(
        to: Long,
        via: TelegramBot,
    ): Deferred<Response<out ReturnType>> = sendAsync(to, via)

    open suspend fun sendAsync(
        to: String,
        via: TelegramBot,
    ): Deferred<Response<out ReturnType>> {
        parameters["chat_id"] = to.toJsonElement()
        return doRequestReturning(via)
    }

    open suspend fun sendReturning(
        to: String,
        via: TelegramBot,
    ): Deferred<Response<out ReturnType>> = sendAsync(to, via)

    open suspend fun sendAsync(
        to: User,
        via: TelegramBot,
    ): Deferred<Response<out ReturnType>> {
        parameters["chat_id"] = to.id.toJsonElement()
        return doRequestReturning(via)
    }

    open suspend fun sendReturning(
        to: User,
        via: TelegramBot,
    ): Deferred<Response<out ReturnType>> = sendAsync(to, via)

    open suspend fun sendAsync(
        to: Chat,
        via: TelegramBot,
    ): Deferred<Response<out ReturnType>> {
        parameters["chat_id"] = to.id.toJsonElement()
        return doRequestReturning(via)
    }

    open suspend fun sendReturning(
        to: Chat,
        via: TelegramBot,
    ): Deferred<Response<out ReturnType>> = sendAsync(to, via)
}
