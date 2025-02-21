package eu.vendeli.tgbot.interfaces.action

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.component.Response
import eu.vendeli.tgbot.utils.internal.toJsonElement
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
    suspend fun send(to: String, via: TelegramBot) {
        parameters["chat_id"] = to.toJsonElement()
        doRequest(via)
    }

    suspend fun send(to: Long, via: TelegramBot) {
        parameters["chat_id"] = to.toJsonElement()
        doRequest(via)
    }

    suspend inline fun send(to: User, via: TelegramBot): Unit = send(to.id, via)

    suspend inline fun send(to: Chat, via: TelegramBot): Unit = send(to.id, via)

    /**
     * Make a request for action returning its [Response].
     *
     * @param to Recipient
     * @param via Instance of the bot through which the request will be made.
     */
    suspend fun sendReturning(
        to: String,
        via: TelegramBot,
    ): Deferred<Response<out ReturnType>> {
        parameters["chat_id"] = to.toJsonElement()
        return doRequestReturning(via)
    }

    suspend fun sendReturning(
        to: Long,
        via: TelegramBot,
    ): Deferred<Response<out ReturnType>> {
        parameters["chat_id"] = to.toJsonElement()
        return doRequestReturning(via)
    }

    suspend inline fun sendReturning(
        to: User,
        via: TelegramBot,
    ): Deferred<Response<out ReturnType>> = sendReturning(to.id, via)

    suspend inline fun sendReturning(
        to: Chat,
        via: TelegramBot,
    ): Deferred<Response<out ReturnType>> = sendReturning(to.id, via)
}
