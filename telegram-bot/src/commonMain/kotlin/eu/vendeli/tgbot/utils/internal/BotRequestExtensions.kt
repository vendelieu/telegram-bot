package eu.vendeli.tgbot.utils.internal

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.types.component.Response
import eu.vendeli.tgbot.utils.common.TgFailureException
import eu.vendeli.tgbot.utils.common.serde
import eu.vendeli.tgbot.utils.serde.primitiveOrNull
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.HeadersBuilder
import io.ktor.http.HttpHeaders
import io.ktor.http.append
import io.ktor.http.content.PartData
import io.ktor.http.contentType
import io.ktor.http.escapeIfNeeded
import io.ktor.http.isSuccess
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonUnquotedLiteral

private inline fun buildHeadersForItem(name: String) = HeadersBuilder()
    .apply {
        append(HttpHeaders.ContentType, ContentType.Application.Json)
        append(HttpHeaders.ContentDisposition, "form-data; name=${name.escapeIfNeeded()}")
    }.build()

@Suppress("OPT_IN_USAGE")
private fun HttpRequestBuilder.formReqBody(
    data: Map<String, JsonElement>,
    multipartData: List<PartData.BinaryItem>,
) {
    if (data.isEmpty() && multipartData.isEmpty()) return
    if (multipartData.isNotEmpty()) {
        val dataParts = data.map {
            val item = it.value.primitiveOrNull?.let { v ->
                if (v.isString) JsonUnquotedLiteral(v.content) else v
            } ?: it.value

            PartData.FormItem(serde.encodeToString(JsonElement.serializer(), item), {}, buildHeadersForItem(it.key))
        }

        setBody(MultiPartFormDataContent(multipartData + dataParts))
    } else {
        setBody(serde.encodeToString(data))
        contentType(ContentType.Application.Json)
    }
}

internal suspend inline fun <T> TelegramBot.makeRequestReturning(
    method: String,
    data: Map<String, JsonElement>,
    returnType: KSerializer<T>,
    multipartData: List<PartData.BinaryItem>,
): Deferred<Response<out T>> = coroutineScope {
    val response = httpClient.post(baseUrl + method) {
        formReqBody(data, multipartData)
    }

    return@coroutineScope async {
        response.bodyAsText().let {
            if (response.status.isSuccess()) serde.decodeFromString(Response.Success.serializer(returnType), it)
            else serde.decodeFromString(Response.Failure.serializer(), it).also { f ->
                val stringFailure = f.toString()
                logger.error { "Request - ${response.call.request.content} received failure response: $stringFailure" }
                if (config.throwExOnActionsFailure) throw TgFailureException(stringFailure)
            }
        }
    }
}

internal suspend inline fun TelegramBot.makeSilentRequest(
    method: String,
    data: Map<String, JsonElement>,
    multipartData: List<PartData.BinaryItem>,
) = httpClient
    .post(baseUrl + method) {
        formReqBody(data, multipartData)
    }.also { call ->
        if (!call.status.isSuccess()) {
            val body = call.bodyAsText()
            logger.error { "Request - ${call.request.content} received failure response: $body" }
            if (config.throwExOnActionsFailure) throw TgFailureException(body)
        }
    }
