package eu.vendeli.tgbot.types.passport

data class EncryptedCredentials(
    val data: String,
    val hash: String,
    val secret: String,
)
