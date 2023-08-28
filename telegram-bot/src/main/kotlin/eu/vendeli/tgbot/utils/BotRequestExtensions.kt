package eu.vendeli.tgbot.utils

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.TelegramBot.Companion.logger
import eu.vendeli.tgbot.TelegramBot.Companion.mapper
import eu.vendeli.tgbot.interfaces.MultipleResponse
import eu.vendeli.tgbot.types.internal.ImplicitFile.InpFile
import eu.vendeli.tgbot.types.internal.InputFile
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
import kotlinx.coroutines.coroutineScope

private val JSON_CONTENT_HEADERS = Headers.build {
    append(HttpHeaders.ContentType, ContentType.Application.Json)
}

private fun Any?.toInputFileOrNull(): InputFile? = when (this) {
    is InpFile -> this.file
    is InputFile -> this
    is Map<*, *> -> get("is_input_file\$telegram_bot")?.runCatching {
        mapper.convertValue(
            this@toInputFileOrNull,
            InputFile::class.java,
        )
    }?.getOrNull()

    else -> null
}

private fun formImplicitReqBody(payload: Map<String, Any?>): Any = MultiPartFormDataContent(
    formData {
        payload.entries.forEach {
            val inputFile = it.value.toInputFileOrNull()
            if (inputFile != null) {
                appendInput(
                    key = it.key,
                    headers = Headers.build {
                        append(HttpHeaders.ContentDisposition, "filename=${inputFile.fileName}")
                        append(HttpHeaders.ContentType, inputFile.contentType)
                    },
                ) { buildPacket { writeFully(inputFile.data) } }
            } else if (it.value != null)
                append(
                    FormPart(
                        it.key,
                        if (it.value is String) it.value!!
                        else mapper.writeValueAsString(it.value),
                        JSON_CONTENT_HEADERS,
                    ),
                )
        }
    },
)

private fun HttpRequestBuilder.formReqBody(payload: Map<String, Any?>, isImplicit: Boolean = false) {
    if (isImplicit) setBody(formImplicitReqBody(payload.also { logger.trace { "RequestBody: $it" } }))
    else {
        setBody(mapper.writeValueAsString(payload).also { logger.debug { "RequestBody: $it" } })
        contentType(ContentType.Application.Json)
    }
}

internal suspend inline fun <T, I : MultipleResponse> TelegramBot.makeRequestAsync(
    method: TgMethod,
    data: Map<String, Any?>? = null,
    returnType: Class<T>,
    wrappedType: Class<I>? = null,
    isImplicit: Boolean = false,
): Deferred<Response<out T>> = coroutineScope {
    val response = httpClient.post(method.getUrl(config.apiHost, token)) {
        if (data != null) formReqBody(data, isImplicit)
    }

    return@coroutineScope handleResponseAsync(response, returnType, wrappedType)
}

internal suspend inline fun TelegramBot.makeSilentRequest(
    method: TgMethod,
    data: Map<String, Any?>? = null,
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
