package eu.vendeli.tgbot.types

data class EncryptedCredentials(
    val data: String,
    val hash: String,
    val secret: String
)
