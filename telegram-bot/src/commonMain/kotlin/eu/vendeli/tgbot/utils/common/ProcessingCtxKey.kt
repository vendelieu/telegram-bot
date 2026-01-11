@file:Suppress("FunctionName")

package eu.vendeli.tgbot.utils.common

import kotlinx.serialization.Serializable

@Serializable
internal enum class ProcessingCtxKey {
    REGEX_MATCH,
}
