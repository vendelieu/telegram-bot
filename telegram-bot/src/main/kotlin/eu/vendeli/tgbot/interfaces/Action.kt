package eu.vendeli.tgbot.interfaces

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.interfaces.features.Feature
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.Response
import kotlinx.coroutines.Deferred

/**
 * Action, see [Actions article](https://github.com/vendelieu/telegram-bot/wiki/Actions)
 *
 * @param ReturnType response type.
 */
interface Action<ReturnType> : TgAction, Feature {
    /**
     * Make a request for action.
     *
     * @param to Recipient
     * @param via Instance of the bot through which the request will be made.
     * @param isInline Whether the request is inline.
     */
    suspend fun send(to: String, via: TelegramBot, isInline: Boolean = false) {
        parameters[if (!isInline) "chat_id" else "inline_message_id"] = to
        via.makeSilentRequest(method, parameters)
    }

    /**
     * Make a request for action.
     *
     * @param to Recipient
     * @param via Instance of the bot through which the request will be made.
     * @param isInline Whether the request is inline.
     */
    suspend fun send(to: Long, via: TelegramBot, isInline: Boolean = false) {
        parameters[if (!isInline) "chat_id" else "inline_message_id"] = to
        via.makeSilentRequest(method, parameters)
    }

    /**
     * Make a request for action.
     *
     * @param to Recipient
     * @param via Instance of the bot through which the request will be made.
     * @param isInline Whether the request is inline.
     */
    suspend fun send(to: User, via: TelegramBot, isInline: Boolean = false) {
        parameters[if (!isInline) "chat_id" else "inline_message_id"] = if (!isInline) to.id else to.username!!
        via.makeSilentRequest(method, parameters)
    }
}

/**
 * Make a request for action returning its [Response].
 *
 * @param to Recipient
 * @param via Instance of the bot through which the request will be made.
 * @param isInline Whether the request is inline.
 */
suspend inline fun <reified ReturnType> Action<ReturnType>.sendAsync(
    to: Long,
    via: TelegramBot,
    isInline: Boolean = false,
): Deferred<Response<out ReturnType>> {
    parameters[if (!isInline) "chat_id" else "inline_message_id"] = to
    return via.makeRequestAsync(method, parameters, ReturnType::class.java, bunchResponseInnerType())
}

/**
 * Make a request for action returning its [Response].
 *
 * @param to Recipient
 * @param via Instance of the bot through which the request will be made.
 * @param isInline Whether the request is inline.
 */
suspend inline fun <reified ReturnType> Action<ReturnType>.sendAsync(
    to: User,
    via: TelegramBot,
    isInline: Boolean = false,
): Deferred<Response<out ReturnType>> {
    parameters[if (!isInline) "chat_id" else "inline_message_id"] = if (!isInline) to.id else to.username!!
    return via.makeRequestAsync(method, parameters, ReturnType::class.java, bunchResponseInnerType())
}

/**
 * Make a request for action returning its [Response].
 *
 * @param to Recipient
 * @param via Instance of the bot through which the request will be made.
 * @param isInline Whether the request is inline.
 */
suspend inline fun <reified ReturnType> Action<ReturnType>.sendAsync(
    to: String,
    via: TelegramBot,
    isInline: Boolean = false,
): Deferred<Response<out ReturnType>> {
    parameters[if (!isInline) "chat_id" else "inline_message_id"] = to
    return via.makeRequestAsync(method, parameters, ReturnType::class.java, bunchResponseInnerType())
}
