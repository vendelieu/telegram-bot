package eu.vendeli.tgbot.types.passport

import kotlinx.serialization.Serializable

@Serializable
data class PassportData(
    val data: List<EncryptedPassportElement>,
    val credentials: EncryptedCredentials,
)
