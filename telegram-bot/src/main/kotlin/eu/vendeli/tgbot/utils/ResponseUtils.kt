package eu.vendeli.tgbot.utils

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import eu.vendeli.tgbot.TelegramBot.Companion.mapper
import eu.vendeli.tgbot.interfaces.MultipleResponse
import eu.vendeli.tgbot.types.internal.Response
import eu.vendeli.tgbot.types.internal.Response.Success
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async

@Suppress("NOTHING_TO_INLINE")
internal inline fun <T, I : MultipleResponse> ObjectMapper.convertSuccessResponse(
    jsonNode: JsonNode,
    type: Class<T>?,
    innerType: Class<I>? = null,
): Success<T> =
    if (innerType == null) convertValue(jsonNode, typeFactory.constructParametricType(Success::class.java, type))
    else Success(convertValue(jsonNode["result"], typeFactory.constructCollectionType(List::class.java, innerType)))

@Suppress("NOTHING_TO_INLINE")
internal inline fun <T, I : MultipleResponse> CoroutineScope.handleResponseAsync(
    response: HttpResponse,
    returnType: Class<T>,
    innerType: Class<I>? = null,
): Deferred<Response<out T>> = async {
    val jsonResponse = mapper.readTree(response.bodyAsText())

    if (jsonResponse["ok"].asBoolean()) mapper.convertSuccessResponse(jsonResponse, returnType, innerType)
    else mapper.convertValue(jsonResponse, Response.Failure::class.java)
}
