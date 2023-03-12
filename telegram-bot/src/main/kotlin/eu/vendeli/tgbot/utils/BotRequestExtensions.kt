package eu.vendeli.tgbot.utils

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.TelegramBot.Companion.logger
import eu.vendeli.tgbot.interfaces.MultipleResponse
import eu.vendeli.tgbot.types.internal.Response
import eu.vendeli.tgbot.types.internal.TgMethod
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.forms.FormPart
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.append
import io.ktor.http.contentType
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeFully
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.coroutineScope

private fun multipartBodyBuilder(
    dataField: String,
    filename: String,
    contentType: ContentType,
    data: ByteArray,
    parameters: Map<String, Any?>? = null,
) = MultiPartFormDataContent(
    formData {
        appendInput(
            key = dataField,
            headers = Headers.build {
                append(HttpHeaders.ContentDisposition, "filename=$filename")
                append(HttpHeaders.ContentType, contentType)
            },
        ) { buildPacket { writeFully(data) } }

        val jsonContentHeaders = Headers.build {
            append(HttpHeaders.ContentType, ContentType.Application.Json)
        }
        parameters?.entries?.forEach { entry ->
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
 * @param method The telegram api method to which the request will be made.
 * @param dataField The name of the field that will contain the media data.
 * @param filename The name of the final file.
 * @param data The data itself.
 * @param parameters Additional parameters.
 * @param contentType The type of content that will be passed in the headers.
 * @param returnType Response data type.
 * @param innerType Parameter used to identify the type in the data array.
 * @return [Deferred]<[Response]<[T]>>
 */
@Suppress("LongParameterList")
suspend fun <T, I : MultipleResponse> TelegramBot.makeRequestAsync(
    method: TgMethod,
    dataField: String,
    filename: String,
    data: ByteArray,
    parameters: Map<String, Any?>? = null,
    contentType: ContentType,
    returnType: Class<T>,
    innerType: Class<I>? = null,
): Deferred<Response<out T>> = coroutineScope {
    val response = httpClient.post(method.toUrl()) {
        setBody(multipartBodyBuilder(dataField, filename, contentType, data, parameters))
        onUpload { bytesSentTotal, contentLength ->
            logger.trace { "Sent $bytesSentTotal bytes from $contentLength, for $method method with $parameters" }
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
 * @param innerType Parameter used to identify the type in the data array.
 * @return [Deferred]<[Response]<[T]>>
 */
internal suspend inline fun <T, I : MultipleResponse> TelegramBot.makeRequestAsync(
    method: TgMethod,
    data: Any? = null,
    returnType: Class<T>,
    innerType: Class<I>? = null,
): Deferred<Response<out T>> = coroutineScope {
    val response = httpClient.post(method.toUrl()) {
        contentType(ContentType.Application.Json)
        setBody(TelegramBot.mapper.writeValueAsString(data))
    }

    return@coroutineScope handleResponseAsync(response, returnType, innerType)
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
}

/**
 * Make a media request without having to return the data.
 *
 * @param method The telegram api method to which the request will be made.
 * @param dataField The name of the field that will contain the media data.
 * @param filename The name of the final file.
 * @param data The data itself.
 * @param parameters Additional parameters.
 * @param contentType The type of content that will be passed in the headers.
 */
@Suppress("LongParameterList")
internal suspend inline fun TelegramBot.makeSilentRequest(
    method: TgMethod,
    dataField: String,
    filename: String,
    data: ByteArray,
    parameters: Map<String, Any?>? = null,
    contentType: ContentType,
) = httpClient.post(method.toUrl()) {
    setBody(multipartBodyBuilder(dataField, filename, contentType, data, parameters))
    onUpload { bytesSentTotal, contentLength ->
        logger.trace { "Sent $bytesSentTotal bytes from $contentLength, for $method method with $parameters" }
    }
    logger.debug { "RequestBody: $parameters" }
}
