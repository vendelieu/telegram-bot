package com.github.vendelieu.tgbot.types

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.vendelieu.tgbot.interfaces.Keyboard

class InlineKeyboardMarkup : Keyboard {
    var inlineKeyboard: MutableList<List<InlineKeyboardButton>> = mutableListOf()

    constructor(vararg buttons: List<InlineKeyboardButton>) {
        inlineKeyboard = mutableListOf(* buttons)
    }

    constructor(vararg buttons: InlineKeyboardButton) {
        inlineKeyboard = mutableListOf(buttons.toList())
    }

    @JsonCreator
    constructor(@JsonProperty("inline_keyboard") keyboard: List<List<InlineKeyboardButton>>) {
        inlineKeyboard = keyboard.toMutableList()
    }

    fun addElement(button: InlineKeyboardButton) {
        inlineKeyboard.add(listOf(button))
    }

    fun addElement(buttons: List<InlineKeyboardButton>) {
        inlineKeyboard.add(buttons)
    }
}
