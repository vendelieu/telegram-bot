package eu.vendeli.tgbot.utils

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.TelegramBot.Companion.logger
import eu.vendeli.tgbot.interfaces.MultipleResponse
import eu.vendeli.tgbot.types.internal.MediaData
import eu.vendeli.tgbot.types.internal.Response
import eu.vendeli.tgbot.types.internal.TgMethod
import io.ktor.client.plugins.onUpload
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
import kotlinx.coroutines.coroutineScope

private fun multipartBodyBuilder(media: MediaData) = MultiPartFormDataContent(
    formData {
        appendInput(
            key = media.dataField,
            headers = Headers.build {
                append(HttpHeaders.ContentDisposition, "filename=${media.name}")
                append(HttpHeaders.ContentType, media.contentType)
            },
        ) { buildPacket { writeFully(media.data) } }

        val jsonContentHeaders = Headers.build {
            append(HttpHeaders.ContentType, ContentType.Application.Json)
        }
        media.parameters?.entries?.forEach { entry ->
            entry.value?.also {
                append(FormPart(entry.key, it, jsonContentHeaders))
            }
        }
    },
)

/**
 * Make a media request with the ability to asynchronously process the response.
 *
 * @param T Generic of response data.
 * @param I Parameter used to identify the type in the data array.
 * @param data The data itself.
 * @param returnType Response data type.
 * @param innerType Parameter used to identify the type in the data array.
 * @return [Deferred]<[Response]<[T]>>
 */
internal suspend fun <T, I : MultipleResponse> TelegramBot.makeRequestAsync(
    returnType: Class<T>,
    innerType: Class<I>? = null,
    data: suspend MediaData.() -> Unit,
): Deferred<Response<out T>> = coroutineScope {
    val media = MediaData().apply { data() }
    val response = httpClient.post(media.method.toUrl()) {
        setBody(multipartBodyBuilder(media))
        onUpload { bytesSentTotal, contentLength ->
            logger.trace {
                "Sent $bytesSentTotal bytes from $contentLength, for $method method with ${media.parameters}"
            }
        }
    }

    return@coroutineScope handleResponseAsync(response, returnType, innerType)
}

/**
 * Make a request with the ability to asynchronously process the response.
 *
 * @param T Generic of response data.
 * @param I Parameter used to identify the type in the data array.
 * @param method The telegram api method to which the request will be made.
 * @param data The data itself.
 * @param returnType Response data type.
 * @param wrappedType Parameter used to identify the type in the data array.
 * @return [Deferred]<[Response]<[T]>>
 */
internal suspend inline fun <T, I : MultipleResponse> TelegramBot.makeRequestAsync(
    method: TgMethod,
    data: Any? = null,
    returnType: Class<T>,
    wrappedType: Class<I>? = null,
): Deferred<Response<out T>> = coroutineScope {
    val response = httpClient.post(method.toUrl()) {
        contentType(ContentType.Application.Json)
        setBody(TelegramBot.mapper.writeValueAsString(data))
    }

    return@coroutineScope handleResponseAsync(response, returnType, wrappedType)
}

/**
 * Make a request without having to return the data.
 *
 * @param method The telegram api method to which the request will be made.
 * @param data The data itself.
 */
internal suspend inline fun TelegramBot.makeSilentRequest(
    method: TgMethod,
    data: Any? = null,
) = httpClient.post(method.toUrl()) {
    val requestBody = TelegramBot.mapper.writeValueAsString(data)
    contentType(ContentType.Application.Json)
    setBody(requestBody)
    logger.debug { "RequestBody: $requestBody" }
}.logFailure()

/**
 * Make a media request without having to return the data.
 *
 */
internal suspend inline fun TelegramBot.makeSilentRequest(
    block: MediaData.() -> Unit,
): HttpResponse {
    val media = MediaData().apply(block)
    return httpClient.post(media.method.toUrl()) {
        setBody(multipartBodyBuilder(media))
        onUpload { bytesSentTotal, contentLength ->
            logger.trace {
                "Sent $bytesSentTotal bytes from $contentLength, for $method method with ${media.parameters}"
            }
        }
        logger.debug { "RequestBody: ${media.parameters}" }
    }.logFailure()
}

internal suspend inline fun HttpResponse.logFailure(): HttpResponse {
    if (!status.isSuccess()) {
        val body = bodyAsText()
        logger.error { "Request - ${request.content} received failure response: $body" }
    }
    return this
}
