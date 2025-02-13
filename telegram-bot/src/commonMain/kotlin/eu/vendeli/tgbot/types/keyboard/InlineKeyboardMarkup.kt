package eu.vendeli.tgbot.types.keyboard

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.marker.Keyboard
import kotlinx.serialization.Serializable

@Serializable
class InlineKeyboardMarkup : Keyboard {
    internal var inlineKeyboard: MutableList<List<InlineKeyboardButton>> = mutableListOf()

    @TgAPI.Ignore
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
