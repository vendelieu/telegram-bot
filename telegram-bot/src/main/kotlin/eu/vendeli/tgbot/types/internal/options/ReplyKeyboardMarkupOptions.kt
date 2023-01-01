package eu.vendeli.tgbot.types.internal.options

data class ReplyKeyboardMarkupOptions(
    var resizeKeyboard: Boolean? = null,
    var oneTimeKeyboard: Boolean? = null,
    var inputFieldPlaceholder: String? = null,
    var selective: Boolean? = null,
    var isPersistent: Boolean? = null,
)
