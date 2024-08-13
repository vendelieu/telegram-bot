package eu.vendeli.tgbot.interfaces.action

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.internal.InternalApi
import eu.vendeli.tgbot.types.internal.Response
import kotlinx.coroutines.Deferred
import kotlinx.serialization.json.JsonElement

interface Request<ReturnType> {
    @InternalApi
    val Request<ReturnType>.parameters: MutableMap<String, JsonElement>

    @InternalApi
    suspend fun Request<ReturnType>.doRequest(bot: TelegramBot)

    @InternalApi
    suspend fun Request<ReturnType>.doRequestReturning(bot: TelegramBot): Deferred<Response<out ReturnType>>
}
