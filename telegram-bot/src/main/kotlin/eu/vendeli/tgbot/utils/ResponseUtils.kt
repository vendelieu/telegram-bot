package eu.vendeli.tgbot.utils

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.CollectionType
import eu.vendeli.tgbot.TelegramBot.Companion.mapper
import eu.vendeli.tgbot.types.internal.Response
import eu.vendeli.tgbot.types.internal.Response.Success
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async

@Suppress("NOTHING_TO_INLINE")
internal inline fun <T> ObjectMapper.convertSuccessResponse(
    jsonNode: JsonNode,
    simpleType: JavaType? = null,
    collectionType: CollectionType? = null,
): Success<T> =
    if (collectionType == null) Success(convertValue(jsonNode["result"], simpleType))
    else Success(convertValue(jsonNode["result"], collectionType))

@Suppress("NOTHING_TO_INLINE")
internal inline fun <T> CoroutineScope.handleResponseAsync(
    response: HttpResponse,
    returnType: JavaType? = null,
    collectionType: CollectionType? = null,
): Deferred<Response<out T>> = async {
    val jsonResponse = mapper.readTree(response.bodyAsText())

    if (jsonResponse["ok"].asBoolean()) mapper.convertSuccessResponse(jsonResponse, returnType, collectionType)
    else mapper.convertValue(jsonResponse, Response.Failure::class.java)
}
