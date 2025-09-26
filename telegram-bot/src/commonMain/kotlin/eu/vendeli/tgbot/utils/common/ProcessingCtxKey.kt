@file:Suppress("FunctionName")

package eu.vendeli.tgbot.utils.common

import kotlinx.serialization.Serializable

@Serializable
enum class ProcessingCtxKey {
    REGEX_MATCH,
    PARSED_PARAMETERS,
    INVOCATION_META,
    INVOCATION_KIND
}
