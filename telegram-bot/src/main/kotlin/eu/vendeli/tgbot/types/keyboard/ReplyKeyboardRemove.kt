package eu.vendeli.tgbot.types.keyboard

import eu.vendeli.tgbot.interfaces.Keyboard
import kotlinx.serialization.Serializable

@Serializable
data class ReplyKeyboardRemove(val selective: Boolean? = null) : Keyboard {
    val removeKeyboard: Boolean = true
}
