package eu.vendeli.tgbot.interfaces

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.internal.Response
import kotlinx.coroutines.Deferred
import kotlinx.serialization.json.JsonElement

interface Request<ReturnType> {
    val Request<ReturnType>.parameters: MutableMap<String, JsonElement>
    suspend fun Request<ReturnType>.doRequest(bot: TelegramBot)
    suspend fun Request<ReturnType>.doRequestAsync(bot: TelegramBot): Deferred<Response<out ReturnType>>
}
