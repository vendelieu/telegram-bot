package eu.vendeli.tgbot.utils

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.core.TelegramUpdateHandler
import eu.vendeli.tgbot.interfaces.MultipleResponse
import eu.vendeli.tgbot.types.internal.RateLimits
import eu.vendeli.tgbot.types.internal.Response
import eu.vendeli.tgbot.types.internal.StructuredRequest
import eu.vendeli.tgbot.types.internal.TgMethod
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.coroutines.*
import java.lang.reflect.Method
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.intrinsics.suspendCoroutineUninterceptedOrReturn

/**
 * Creates new coroutine context from parent one and adds supervisor job.
 *
 * @param parentContext Context that will be merged with the created one.
 */
internal class CreateNewCoroutineContext(parentContext: CoroutineContext) : CoroutineScope {
    override val coroutineContext: CoroutineContext =
        parentContext + SupervisorJob(parentContext[Job]) + CoroutineName("TgBot")
}

internal fun String.parseQuery(): StructuredRequest {
    val reqParams = split('?')

    return StructuredRequest(
        command = reqParams.first(),
        params = reqParams.last().split('&').associate { it.substringBefore('=') to it.substringAfter('=') }
    )
}

internal fun String.parseKeyValueBySpace(): StructuredRequest {
    val parsedString = split(' ')

    return StructuredRequest(
        parsedString.first(),
        parsedString.chunked(2).associate { it.first() to (it.lastOrNull() ?: "") }
    )
}

internal suspend fun Method.invokeSuspend(obj: Any, vararg args: Any?): Any? =
    suspendCoroutineUninterceptedOrReturn { cont ->
        invoke(obj, *args, cont)
    }

internal suspend fun TelegramUpdateHandler.checkIsLimited(
    limits: RateLimits,
    telegramId: Long?,
    actionId: String? = null
): Boolean {
    if(limits.period == 0L && limits.rate == 0L || telegramId == null) return false

    logger.trace("Checking the request for exceeding the limits${if (actionId != null) " for ${actionId}}" else ""}.")
    if (rateLimiter.isLimited(limits, telegramId, actionId)) {
        logger.info("User #$telegramId has exceeded the request limit${if (actionId != null) " for ${actionId}}" else ""}.")
        rateLimiter.exceededLimitResponse(telegramId, bot)
        return true
    }
    return false
}

suspend fun <T, I : MultipleResponse> TelegramBot.makeBunchMediaRequestAsync(
    method: TgMethod,
    files: Map<String, ByteArray>,
    parameters: Map<String, Any?>? = null,
    contentType: ContentType,
    returnType: Class<T>,
    innerType: Class<I>? = null,
): Deferred<Response<out T>> = coroutineScope {
    val response = httpClient.post(method.toUrl()) {
        setBody(MultiPartFormDataContent(
            formData {
                files.forEach {
                    append(it.key, it.value, Headers.build {
                        append(HttpHeaders.ContentDisposition, "filename=${it.key}")
                        append(HttpHeaders.ContentType, contentType)
                    })
                }
                parameters?.entries?.forEach { entry ->
                    entry.value?.also { append(FormPart(entry.key, TelegramBot.mapper.writeValueAsString(it))) }
                }
            }
        ))
        onUpload { bytesSentTotal, contentLength ->
            logger.trace("Sent $bytesSentTotal bytes from $contentLength, for $method with $parameters")
        }
    }

    return@coroutineScope handleResponseAsync(response, returnType, innerType)
}

suspend fun TelegramBot.makeSilentBunchMediaRequest(
    method: TgMethod,
    files: Map<String, ByteArray>,
    parameters: Map<String, Any?>? = null,
    contentType: ContentType
) {
    httpClient.post(method.toUrl()) {
        setBody(MultiPartFormDataContent(
            formData {
                files.forEach {
                    append(it.key, it.value, Headers.build {
                        append(HttpHeaders.ContentDisposition, "filename=${it.key}")
                        append(HttpHeaders.ContentType, contentType)
                    })
                }
                parameters?.entries?.forEach { entry ->
                    entry.value?.also { append(FormPart(entry.key, TelegramBot.mapper.writeValueAsString(it))) }
                }
            }
        ))
        onUpload { bytesSentTotal, contentLength ->
            logger.trace("Sent $bytesSentTotal bytes from $contentLength, for $method with $parameters")
        }
    }
}

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
