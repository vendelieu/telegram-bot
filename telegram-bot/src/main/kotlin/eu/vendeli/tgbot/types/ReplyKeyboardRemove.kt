package eu.vendeli.tgbot.types

import eu.vendeli.tgbot.interfaces.Keyboard

data class ReplyKeyboardRemove(val selective: Boolean? = null) : Keyboard {
    val removeKeyboard: Boolean = true
}
