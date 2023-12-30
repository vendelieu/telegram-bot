package eu.vendeli.tgbot.types.keyboard

data class KeyboardButtonRequestUsers(
    val requestId: Int,
    val userIsBot: Boolean? = null,
    val userIsPremium: Boolean? = null,
    val maxQuantity: Int? = null,
)
