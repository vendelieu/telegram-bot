package eu.vendeli.tgbot.interfaces.action

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.internal.KtGramInternal
import eu.vendeli.tgbot.types.internal.Response
import kotlinx.coroutines.Deferred
import kotlinx.serialization.json.JsonElement

interface Request<ReturnType> {
    @KtGramInternal
    val Request<ReturnType>.parameters: MutableMap<String, JsonElement>

    @KtGramInternal
    suspend fun Request<ReturnType>.doRequest(bot: TelegramBot)

    @KtGramInternal
    suspend fun Request<ReturnType>.doRequestReturning(bot: TelegramBot): Deferred<Response<out ReturnType>>
}
