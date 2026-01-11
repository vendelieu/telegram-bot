@file:Suppress("FunctionName")

package eu.vendeli.tgbot.utils.common

import kotlinx.serialization.Serializable

@Serializable
internal sealed class ProcessingCtxKey {
    data object RegexMatch : ProcessingCtxKey()

    data class Custom(val name: String) : ProcessingCtxKey()
}
