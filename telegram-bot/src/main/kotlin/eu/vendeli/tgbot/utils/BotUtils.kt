package eu.vendeli.tgbot.utils

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import eu.vendeli.tgbot.interfaces.MultipleResponse
import eu.vendeli.tgbot.types.internal.Response
import eu.vendeli.tgbot.types.internal.StructuredRequest
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
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

internal object StringManipulations {
    private val regex = "([a-z])([A-Z]+)".toRegex()
    private const val replacement = "$1_$2"

    fun camel2SnakeCase(str: String) = regex.replace(str, replacement).lowercase()
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
