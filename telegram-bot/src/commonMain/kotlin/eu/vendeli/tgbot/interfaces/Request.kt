package eu.vendeli.tgbot.interfaces

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.internal.Response
import kotlinx.coroutines.Deferred
import kotlinx.serialization.json.JsonElement

interface Request<ReturnType> {
    fun Request<ReturnType>.getParameters(): MutableMap<String, JsonElement> = TODO()
    suspend fun Request<ReturnType>.doRequest(bot: TelegramBot): Unit = TODO()
    suspend fun Request<ReturnType>.doRequestAsync(bot: TelegramBot): Deferred<Response<out ReturnType>> = TODO()
}
