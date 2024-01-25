package eu.vendeli.tgbot.types.keyboard

import kotlinx.serialization.Serializable

@Serializable
data class LoginUrl(
    val url: String,
    val forwardText: String? = null,
    val botUsername: String? = null,
    val requestWriteAccess: Boolean? = null,
)
