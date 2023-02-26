package eu.vendeli.tgbot.types

data class KeyboardButtonRequestUser(
    val requestId: Int,
    val userIsBot: Boolean? = null,
    val userIsPremium: Boolean? = null
)
