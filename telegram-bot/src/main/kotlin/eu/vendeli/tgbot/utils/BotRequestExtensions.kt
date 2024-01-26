package eu.vendeli.tgbot.utils

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.TelegramBot.Companion.logger
import eu.vendeli.tgbot.types.internal.Response
import eu.vendeli.tgbot.types.internal.TgMethod
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
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
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.JsonElement

@Suppress("NOTHING_TO_INLINE")
private inline fun buildHeadersForItem(name: String) = HeadersBuilder().apply {
    append(HttpHeaders.ContentType, ContentType.Application.Json)
    append(HttpHeaders.ContentDisposition, "form-data; name=${name.escapeIfNeeded()}")
}.build()

private fun HttpRequestBuilder.formReqBody(
    data: Map<String, JsonElement>,
    multipartData: List<PartData.BinaryItem>,
) {
    if (data.isEmpty() && multipartData.isEmpty()) return
    if (multipartData.isNotEmpty()) {
        val dataParts = data.also { logger.debug { "RequestBody: $it" } }.map {
            PartData.FormItem(it.value.toString(), {}, buildHeadersForItem(it.key))
        }

        setBody(MultiPartFormDataContent(multipartData + dataParts))
    } else {
        setBody(serde.encodeToString(data).also { logger.debug { "RequestBody: $it" } })
        contentType(ContentType.Application.Json)
    }
}

private suspend inline fun <T> HttpResponse.toResult(type: KSerializer<T>) = bodyAsText().let {
    if (status.isSuccess()) serde.decodeFromString(Response.Success.serializer(type), it)
    else serde.decodeFromString(Response.Failure.serializer(), it)
}

internal suspend inline fun <T> TelegramBot.makeRequestAsync(
    method: TgMethod,
    data: Map<String, JsonElement>,
    returnType: KSerializer<T>,
    multipartData: List<PartData.BinaryItem>,
): Deferred<Response<out T>> = coroutineScope {
    val response = httpClient.post(method.getUrl(config.apiHost, token)) {
        formReqBody(data, multipartData)
    }

    return@coroutineScope async { response.toResult(returnType) }
}

internal suspend inline fun TelegramBot.makeSilentRequest(
    method: TgMethod,
    data: Map<String, JsonElement>,
    multipartData: List<PartData.BinaryItem>,
) = httpClient.post(method.getUrl(config.apiHost, token)) {
    formReqBody(data, multipartData)
}.logFailure()

internal suspend inline fun HttpResponse.logFailure(): HttpResponse {
    if (!status.isSuccess()) {
        val body = bodyAsText()
        logger.error { "Request - ${request.content} received failure response: $body" }
    }
    return this
}
