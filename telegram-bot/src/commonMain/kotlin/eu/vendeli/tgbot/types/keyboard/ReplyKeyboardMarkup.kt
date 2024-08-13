package eu.vendeli.tgbot.types.keyboard

import eu.vendeli.tgbot.interfaces.marker.Keyboard
import kotlinx.serialization.Serializable

@Suppress("unused")
@Serializable
class ReplyKeyboardMarkup : Keyboard {
    internal var keyboard: List<List<KeyboardButton>> = emptyList()

    var resizeKeyboard: Boolean? = null
    var oneTimeKeyboard: Boolean? = null
    var inputFieldPlaceholder: String? = null
    var selective: Boolean? = null
    var isPersistent: Boolean? = null

    constructor(vararg buttons: List<KeyboardButton>) {
        keyboard = buttons.asList()
    }

    constructor(vararg buttons: KeyboardButton) {
        keyboard = listOf(buttons.asList())
    }

    constructor(
        keyboard: List<List<KeyboardButton>>,
        resizeKeyboard: Boolean? = null,
        oneTimeKeyboard: Boolean? = null,
        inputFieldPlaceholder: String? = null,
        selective: Boolean? = null,
        isPersistent: Boolean? = null,
    ) {
        this.keyboard = keyboard.toMutableList()
        this.resizeKeyboard = resizeKeyboard
        this.oneTimeKeyboard = oneTimeKeyboard
        this.inputFieldPlaceholder = inputFieldPlaceholder
        this.selective = selective
        this.isPersistent = isPersistent
    }
}
