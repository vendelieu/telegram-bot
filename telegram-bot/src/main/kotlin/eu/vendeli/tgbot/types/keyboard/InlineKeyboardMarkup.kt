package eu.vendeli.tgbot.types.keyboard

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import eu.vendeli.tgbot.interfaces.Keyboard

class InlineKeyboardMarkup : Keyboard {
    @JsonProperty("inline_keyboard")
    @get:JsonIgnore
    internal var inlineKeyboard: MutableList<List<InlineKeyboardButton>> = mutableListOf()

    constructor(vararg buttons: InlineKeyboardButton) {
        inlineKeyboard = mutableListOf(buttons.asList())
    }

    @JsonCreator
    constructor(
        @JsonProperty("inline_keyboard") keyboard: List<List<InlineKeyboardButton>>,
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
