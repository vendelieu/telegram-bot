package eu.vendeli.tgbot.types.passport

data class PassportData(
    val data: List<EncryptedPassportElement>,
    val credentials: EncryptedCredentials,
)
