package com.github.vendelieu.tgbot.interfaces

import com.github.vendelieu.tgbot.TelegramBot
import com.github.vendelieu.tgbot.interfaces.features.Feature
import com.github.vendelieu.tgbot.types.internal.Response
import kotlinx.coroutines.Deferred

interface SimpleAction<ReturnType> : TgAction, Feature {
    suspend fun send(to: TelegramBot) {
        to.makeSilentRequest(method, parameters)
    }
}

suspend inline fun <reified ReturnType> SimpleAction<ReturnType>.sendAsync(to: TelegramBot): Deferred<Response<ReturnType>> =
    to.makeRequestAsync(method, parameters, ReturnType::class.java, bunchResponseInnerType())
