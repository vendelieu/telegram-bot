package eu.vendeli.tgbot.types

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import eu.vendeli.tgbot.interfaces.Keyboard

@Suppress("unused")
class ReplyKeyboardMarkup : Keyboard {
    var keyboard: MutableList<List<KeyboardButton>> = mutableListOf()
    var resizeKeyboard: Boolean? = null
    var oneTimeKeyboard: Boolean? = null
    var inputFieldPlaceholder: String? = null
    var selective: Boolean? = null

    constructor(vararg buttons: List<KeyboardButton>) {
        keyboard.addAll(buttons)
    }

    constructor(vararg buttons: KeyboardButton) {
        keyboard.add(buttons.toList())
    }

    constructor(
        resizeKeyboard: Boolean? = null,
        oneTimeKeyboard: Boolean? = null,
        inputFieldPlaceholder: String? = null,
        selective: Boolean? = null,
        vararg buttons: List<KeyboardButton>,
    ) {
        this.resizeKeyboard = resizeKeyboard
        this.oneTimeKeyboard = oneTimeKeyboard
        this.inputFieldPlaceholder = inputFieldPlaceholder
        this.selective = selective
        keyboard.addAll(buttons)
    }

    constructor(
        resizeKeyboard: Boolean? = null,
        oneTimeKeyboard: Boolean? = null,
        inputFieldPlaceholder: String? = null,
        selective: Boolean? = null,
        vararg buttons: KeyboardButton,
    ) {
        this.resizeKeyboard = resizeKeyboard
        this.oneTimeKeyboard = oneTimeKeyboard
        this.inputFieldPlaceholder = inputFieldPlaceholder
        this.selective = selective
        keyboard.add(buttons.toList())
    }

    @JsonCreator
    constructor(
        @JsonProperty("keyboard") keyboard: MutableList<List<KeyboardButton>>,
        @JsonProperty("resize_keyboard") resizeKeyboard: Boolean? = null,
        @JsonProperty("one_time_keyboard") oneTimeKeyboard: Boolean? = null,
        @JsonProperty("input_field_placeholder") inputFieldPlaceholder: String? = null,
        @JsonProperty("selective") selective: Boolean? = null,
    ) {
        this.keyboard = keyboard
        this.resizeKeyboard = resizeKeyboard
        this.oneTimeKeyboard = oneTimeKeyboard
        this.inputFieldPlaceholder = inputFieldPlaceholder
        this.selective = selective
    }
}
