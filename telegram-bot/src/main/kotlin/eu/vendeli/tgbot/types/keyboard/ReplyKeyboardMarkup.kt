package eu.vendeli.tgbot.types.keyboard

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import eu.vendeli.tgbot.interfaces.Keyboard

@Suppress("unused")
class ReplyKeyboardMarkup : Keyboard {
    @JsonProperty("keyboard")
    @get:JsonIgnore
    internal var kbd: MutableList<List<KeyboardButton>> = mutableListOf()

    @get:JsonIgnore
    val keyboard: List<List<KeyboardButton>> get() = kbd

    var resizeKeyboard: Boolean? = null
    var oneTimeKeyboard: Boolean? = null
    var inputFieldPlaceholder: String? = null
    var selective: Boolean? = null
    var isPersistent: Boolean? = null

    constructor(vararg buttons: List<KeyboardButton>) {
        kbd.addAll(buttons)
    }

    constructor(vararg buttons: KeyboardButton) {
        kbd.add(buttons.asList())
    }

    @JsonCreator
    constructor(
        @JsonProperty("keyboard") keyboard: List<List<KeyboardButton>>,
        @JsonProperty("resize_keyboard") resizeKeyboard: Boolean? = null,
        @JsonProperty("one_time_keyboard") oneTimeKeyboard: Boolean? = null,
        @JsonProperty("input_field_placeholder") inputFieldPlaceholder: String? = null,
        @JsonProperty("selective") selective: Boolean? = null,
        @JsonProperty("is_persistent") isPersistent: Boolean? = null,
    ) {
        this.kbd = keyboard.toMutableList()
        this.resizeKeyboard = resizeKeyboard
        this.oneTimeKeyboard = oneTimeKeyboard
        this.inputFieldPlaceholder = inputFieldPlaceholder
        this.selective = selective
        this.isPersistent = isPersistent
    }
}
