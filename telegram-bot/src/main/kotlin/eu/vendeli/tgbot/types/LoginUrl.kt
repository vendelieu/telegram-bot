package eu.vendeli.tgbot.types

data class LoginUrl(
    val url: String,
    val forwardText: String? = null,
    val botUsername: String? = null,
    val requestWriteAccess: Boolean? = null
)
