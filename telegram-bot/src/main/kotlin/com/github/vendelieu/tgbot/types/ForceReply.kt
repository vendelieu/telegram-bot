package com.github.vendelieu.tgbot.types

import com.github.vendelieu.tgbot.interfaces.Keyboard

data class ForceReply(
    val inputFieldPlaceHolder: String? = null,
    val selective: Boolean? = null,
) : Keyboard {
    val forceReply: Boolean = true
}
