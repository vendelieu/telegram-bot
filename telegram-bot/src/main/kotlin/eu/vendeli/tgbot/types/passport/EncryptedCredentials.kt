package eu.vendeli.tgbot.types.passport

import kotlinx.serialization.Serializable

@Serializable
data class EncryptedCredentials(
    val data: String,
    val hash: String,
    val secret: String,
)
