package com.github.vendelieu.tgbot.utils

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.vendelieu.tgbot.interfaces.MultipleResponse
import com.github.vendelieu.tgbot.types.internal.Success
import com.github.vendelieu.tgbot.types.internal.Uri
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

internal fun String.parseUri(): Uri {
    val reqParams = split('?')

    return Uri(
        request = reqParams.first(),
        params = reqParams.last().split('&').associate { it.substringBefore('=') to it.substringAfter('=') }
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
): Success<T> =
    if (innerType == null) convertValue(jsonNode, typeFactory.constructParametricType(Success::class.java, type))
    else Success(
        ok = true,
        result = convertValue(jsonNode["result"], typeFactory.constructCollectionType(List::class.java, innerType))
    )
