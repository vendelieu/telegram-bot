package eu.vendeli.tgbot.types.keyboard

import eu.vendeli.tgbot.interfaces.Keyboard
import kotlinx.serialization.Transient

@Suppress("unused")
class ReplyKeyboardMarkup : Keyboard {
    @Transient
    internal var kbd: MutableList<List<KeyboardButton>> = mutableListOf()
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

    constructor(
        keyboard: List<List<KeyboardButton>>,
        resizeKeyboard: Boolean? = null,
        oneTimeKeyboard: Boolean? = null,
        inputFieldPlaceholder: String? = null,
        selective: Boolean? = null,
        isPersistent: Boolean? = null,
    ) {
        this.kbd = keyboard.toMutableList()
        this.resizeKeyboard = resizeKeyboard
        this.oneTimeKeyboard = oneTimeKeyboard
        this.inputFieldPlaceholder = inputFieldPlaceholder
        this.selective = selective
        this.isPersistent = isPersistent
    }
}
