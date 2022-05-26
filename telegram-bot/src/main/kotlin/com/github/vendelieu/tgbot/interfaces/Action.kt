package com.github.vendelieu.tgbot.interfaces

import com.github.vendelieu.tgbot.TelegramBot
import com.github.vendelieu.tgbot.interfaces.features.Feature
import com.github.vendelieu.tgbot.types.User
import com.github.vendelieu.tgbot.types.internal.Response
import kotlinx.coroutines.Deferred

interface Action<ReturnType> : TgAction, Feature {
    suspend fun send(to: String, via: com.github.vendelieu.tgbot.TelegramBot, isInline: Boolean = false) {
        parameters[if (!isInline) "chat_id" else "inline_message_id"] = to
        via.makeSilentRequest(method, parameters)
    }

    suspend fun send(to: Long, via: com.github.vendelieu.tgbot.TelegramBot, isInline: Boolean = false) {
        parameters[if (!isInline) "chat_id" else "inline_message_id"] = to
        via.makeSilentRequest(method, parameters)
    }

    suspend fun send(to: User, via: com.github.vendelieu.tgbot.TelegramBot, isInline: Boolean = false) {
        parameters[if (!isInline) "chat_id" else "inline_message_id"] = if (!isInline) to.id else to.username!!
        via.makeSilentRequest(method, parameters)
    }
}

suspend inline fun <reified ReturnType> Action<ReturnType>.sendAsync(
    to: Long,
    via: com.github.vendelieu.tgbot.TelegramBot,
    isInline: Boolean = false,
): Deferred<Response<ReturnType>> {
    parameters[if (!isInline) "chat_id" else "inline_message_id"] = to
    return via.makeRequestAsync(method, parameters, ReturnType::class.java, bunchResponseInnerType())
}

suspend inline fun <reified ReturnType> Action<ReturnType>.sendAsync(
    to: User,
    via: com.github.vendelieu.tgbot.TelegramBot,
    isInline: Boolean = false,
): Deferred<Response<ReturnType>> {
    parameters[if (!isInline) "chat_id" else "inline_message_id"] = if (!isInline) to.id else to.username!!
    return via.makeRequestAsync(method, parameters, ReturnType::class.java, bunchResponseInnerType())
}

suspend inline fun <reified ReturnType> Action<ReturnType>.sendAsync(
    to: String,
    via: com.github.vendelieu.tgbot.TelegramBot,
    isInline: Boolean = false,
): Deferred<Response<ReturnType>> {
    parameters[if (!isInline) "chat_id" else "inline_message_id"] = to
    return via.makeRequestAsync(method, parameters, ReturnType::class.java, bunchResponseInnerType())
}
