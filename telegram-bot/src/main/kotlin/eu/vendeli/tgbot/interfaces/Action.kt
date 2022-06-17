package eu.vendeli.tgbot.interfaces

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.interfaces.features.Feature
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.Response
import kotlinx.coroutines.Deferred

interface Action<ReturnType> : TgAction, Feature {
    suspend fun send(to: String, via: TelegramBot, isInline: Boolean = false) {
        parameters[if (!isInline) "chat_id" else "inline_message_id"] = to
        via.makeSilentRequest(method, parameters)
    }

    suspend fun send(to: Long, via: TelegramBot, isInline: Boolean = false) {
        parameters[if (!isInline) "chat_id" else "inline_message_id"] = to
        via.makeSilentRequest(method, parameters)
    }

    suspend fun send(to: User, via: TelegramBot, isInline: Boolean = false) {
        parameters[if (!isInline) "chat_id" else "inline_message_id"] = if (!isInline) to.id else to.username!!
        via.makeSilentRequest(method, parameters)
    }
}

suspend inline fun <reified ReturnType> Action<ReturnType>.sendAsync(
    to: Long,
    via: TelegramBot,
    isInline: Boolean = false,
): Deferred<Response<ReturnType>> {
    parameters[if (!isInline) "chat_id" else "inline_message_id"] = to
    return via.makeRequestAsync(method, parameters, ReturnType::class.java, bunchResponseInnerType())
}

suspend inline fun <reified ReturnType> Action<ReturnType>.sendAsync(
    to: User,
    via: TelegramBot,
    isInline: Boolean = false,
): Deferred<Response<ReturnType>> {
    parameters[if (!isInline) "chat_id" else "inline_message_id"] = if (!isInline) to.id else to.username!!
    return via.makeRequestAsync(method, parameters, ReturnType::class.java, bunchResponseInnerType())
}

suspend inline fun <reified ReturnType> Action<ReturnType>.sendAsync(
    to: String,
    via: TelegramBot,
    isInline: Boolean = false,
): Deferred<Response<ReturnType>> {
    parameters[if (!isInline) "chat_id" else "inline_message_id"] = to
    return via.makeRequestAsync(method, parameters, ReturnType::class.java, bunchResponseInnerType())
}
