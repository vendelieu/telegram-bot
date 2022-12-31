package eu.vendeli.tgbot.types

import eu.vendeli.tgbot.interfaces.Keyboard

data class ForceReply(
    val inputFieldPlaceHolder: String? = null,
    val selective: Boolean? = null,
) : Keyboard {
    @Suppress("unused", "MemberNameEqualsClassName")
    val forceReply: Boolean = true
}
