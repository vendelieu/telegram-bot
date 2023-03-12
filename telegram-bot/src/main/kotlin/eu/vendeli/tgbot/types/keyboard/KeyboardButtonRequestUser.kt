package eu.vendeli.tgbot.types.keyboard

data class KeyboardButtonRequestUser(
    val requestId: Int,
    val userIsBot: Boolean? = null,
    val userIsPremium: Boolean? = null,
)
