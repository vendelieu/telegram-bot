package eu.vendeli.tgbot.types.options

import kotlinx.serialization.Serializable

@Serializable
data class ReplyKeyboardMarkupOptions(
    var resizeKeyboard: Boolean? = null,
    var oneTimeKeyboard: Boolean? = null,
    var inputFieldPlaceholder: String? = null,
    var selective: Boolean? = null,
    var isPersistent: Boolean? = null,
)
