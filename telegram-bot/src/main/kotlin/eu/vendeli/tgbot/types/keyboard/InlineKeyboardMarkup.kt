package eu.vendeli.tgbot.types.keyboard

import eu.vendeli.tgbot.interfaces.Keyboard
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class InlineKeyboardMarkup : Keyboard {
    @Transient
    internal var inlineKeyboard: MutableList<List<InlineKeyboardButton>> = mutableListOf()

    val keyboard: List<List<InlineKeyboardButton>> get() = inlineKeyboard

    constructor(vararg buttons: InlineKeyboardButton) {
        inlineKeyboard = mutableListOf(buttons.asList())
    }

    constructor(
        keyboard: List<List<InlineKeyboardButton>>,
    ) {
        inlineKeyboard = keyboard.toMutableList()
    }

    fun addElement(button: InlineKeyboardButton) {
        inlineKeyboard.add(listOf(button))
    }

    fun addElement(buttons: List<InlineKeyboardButton>) {
        inlineKeyboard.add(buttons)
    }
}
