package eu.vendeli.tgbot.utils

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.enums.HttpLogLevel
import eu.vendeli.tgbot.interfaces.MultipleResponse
import eu.vendeli.tgbot.types.internal.Response
import eu.vendeli.tgbot.types.internal.StructuredRequest
import io.ktor.client.request.*
import io.ktor.client.statement.*
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


internal suspend fun TelegramBot.botHttpRequest(url: String? = null, method: HttpMethod = HttpMethod.Get, builder: HttpRequestBuilder.() -> Unit = {}): HttpResponse? {
    val r = maxRequestRetry.let { if (it < 1 ) 1 else it }
    repeat(r) {
        try {
            return httpClient.request {
                this.method = method
                if (url != null) url(url)
                apply(builder)
            }
        } catch (e: Throwable) {
            if (e is CancellationException) throw e
            if (!disableHttpLogs && httpLogLevel != HttpLogLevel.NONE)
                logger.error(e.stackTraceToString())
        }
    }
    return null
}