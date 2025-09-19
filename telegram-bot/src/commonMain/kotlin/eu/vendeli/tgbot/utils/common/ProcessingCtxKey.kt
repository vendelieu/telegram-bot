@file:Suppress("FunctionName")

package eu.vendeli.tgbot.utils.common

import eu.vendeli.tgbot.types.component.ProcessedUpdate
import kotlinx.serialization.Serializable

@Serializable
enum class ProcessingCtxKey {
    REGEX_MATCH, PARSED_PARAMETERS
}

fun ProcessedUpdate._getCtxValue(key: ProcessingCtxKey): Any? = processingCtx[key]
fun ProcessedUpdate._getRegexMatch(): MatchResult? = _getCtxValue(ProcessingCtxKey.REGEX_MATCH).safeCast()
fun ProcessedUpdate._getParsedParameters(): Map<String, String>? =
    _getCtxValue(ProcessingCtxKey.PARSED_PARAMETERS).safeCast()
