package eu.vendeli.tgbot.types.keyboard

import eu.vendeli.tgbot.interfaces.Keyboard
import kotlinx.serialization.Serializable

@Serializable
data class ForceReply(
    val inputFieldPlaceholder: String? = null,
    val selective: Boolean? = null,
) : Keyboard {
    @Suppress("unused", "MemberNameEqualsClassName")
    val forceReply: Boolean = true
}
