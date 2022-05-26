package com.github.vendelieu.tgbot.types

import com.github.vendelieu.tgbot.interfaces.Keyboard

data class ReplyKeyboardRemove(val selective: Boolean? = null) : Keyboard {
    val removeKeyboard: Boolean = true
}
