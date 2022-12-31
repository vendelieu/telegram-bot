package eu.vendeli.tgbot.utils

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.interfaces.MultipleResponse
import eu.vendeli.tgbot.types.internal.Response
import io.ktor.client.statement.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async

internal fun <T, I : MultipleResponse> ObjectMapper.convertSuccessResponse(
    jsonNode: JsonNode,
    type: Class<T>?,
    innerType: Class<I>? = null,
): Response.Success<T> =
    if (innerType == null) convertValue(
        jsonNode,
        typeFactory.constructParametricType(Response.Success::class.java, type)
    )
    else Response.Success(
        result = convertValue(jsonNode["result"], typeFactory.constructCollectionType(List::class.java, innerType))
    )

internal fun <T, I : MultipleResponse> CoroutineScope.handleResponseAsync(
    response: HttpResponse,
    returnType: Class<T>,
    innerType: Class<I>? = null,
): Deferred<Response<out T>> = async {
    val jsonResponse = TelegramBot.mapper.readTree(response.bodyAsText())

    if (jsonResponse["ok"].asBoolean()) TelegramBot.mapper.convertSuccessResponse(jsonResponse, returnType, innerType)
    else TelegramBot.mapper.convertValue(jsonResponse, Response.Failure::class.java)
}