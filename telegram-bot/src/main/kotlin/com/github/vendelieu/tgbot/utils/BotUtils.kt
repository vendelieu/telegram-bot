package com.github.vendelieu.tgbot.utils

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.vendelieu.tgbot.interfaces.MultipleResponse
import com.github.vendelieu.tgbot.types.internal.Success
import com.github.vendelieu.tgbot.types.internal.Uri
import java.lang.reflect.Method
import kotlin.coroutines.intrinsics.suspendCoroutineUninterceptedOrReturn

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
): Success<T> {
    return if (innerType == null) convertValue(jsonNode, typeFactory.constructParametricType(Success::class.java, type))
    else Success(
        ok = true,
        result = convertValue(jsonNode["result"], typeFactory.constructCollectionType(List::class.java, innerType))
    )
}
