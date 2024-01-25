package eu.vendeli.tgbot.utils

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.TelegramBot.Companion.logger
import eu.vendeli.tgbot.types.internal.Response
import eu.vendeli.tgbot.types.internal.TgMethod
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.forms.FormPart
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.append
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeFully
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.KSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

private val JSON_CONTENT_HEADERS = Headers.build {
    append(HttpHeaders.ContentType, ContentType.Application.Json)
}

private fun formImplicitReqBody(payload: Map<String, JsonElement>): Any = MultiPartFormDataContent(
    formData {
        payload.entries.forEach { i ->
            i.value.takeIf { e ->
                e is JsonObject && e.jsonObject["is_input_file"]?.jsonPrimitive?.booleanOrNull == true
            }?.let {
                appendInput(
                    key = i.key,
                    headers = Headers.build {
                        append(
                            HttpHeaders.ContentDisposition,
                            "filename=${i.value.jsonObject["file_name"]!!.jsonPrimitive.content}",
                        )
                        append(HttpHeaders.ContentType, i.value.jsonObject["content_type"]!!.jsonPrimitive.content)
                    },
                ) {
                    buildPacket {
                        writeFully(i.value.jsonObject["data"]!!.jsonArray.map { it.jsonPrimitive.int }.toIntArray())
                    }
                }
            } ?: append(
                FormPart(
                    i.key,
                    serde.encodeToString(i.value),
                    JSON_CONTENT_HEADERS,
                ),
            )
        }
    },
)

private fun HttpRequestBuilder.formReqBody(payload: Map<String, JsonElement>, isImplicit: Boolean = false) {
    if (isImplicit) {
        setBody(formImplicitReqBody(payload.also { logger.trace { "RequestBody: $it" } }))
    } else {
        setBody(serde.encodeToString(payload).also { logger.debug { "RequestBody: $it" } })
        contentType(ContentType.Application.Json)
    }
}

private fun <T> String.toResponse(type: KSerializer<T>) = serde.decodeFromString(JsonObject.serializer(), this).let {
    if (it.jsonObject["ok"]?.jsonPrimitive?.booleanOrNull == true) serde.decodeFromJsonElement(
        Response.Success.serializer(type),
        it,
    ) else serde.decodeFromJsonElement(Response.Failure.serializer(), it)
}

internal suspend inline fun <T> TelegramBot.makeRequestAsync(
    method: TgMethod,
    data: Map<String, JsonElement>? = null,
    returnType: KSerializer<T>,
    isImplicit: Boolean = false,
): Deferred<Response<out T>> = coroutineScope {
    val response = httpClient.post(method.getUrl(config.apiHost, token)) {
        if (data != null) formReqBody(data, isImplicit)
    }

    return@coroutineScope async { response.bodyAsText().toResponse(returnType) }
}

internal suspend inline fun TelegramBot.makeSilentRequest(
    method: TgMethod,
    data: Map<String, JsonElement>? = null,
    isImplicit: Boolean = false,
) = httpClient.post(method.getUrl(config.apiHost, token)) {
    if (data != null) formReqBody(data, isImplicit)
}.logFailure()

internal suspend inline fun HttpResponse.logFailure(): HttpResponse {
    if (!status.isSuccess()) {
        val body = bodyAsText()
        logger.error { "Request - ${request.content} received failure response: $body" }
    }
    return this
}
