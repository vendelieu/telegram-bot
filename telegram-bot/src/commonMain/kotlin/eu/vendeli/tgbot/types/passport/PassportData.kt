package eu.vendeli.tgbot.types.passport

import kotlinx.serialization.Serializable

/**
 * Describes Telegram Passport data shared with the bot by the user.
 * @property data Array with information about documents and other Telegram Passport elements that was shared with the bot
 * @property credentials Encrypted credentials required to decrypt the data
 * Api reference: https://core.telegram.org/bots/api#passportdata
*/
@Serializable
data class PassportData(
    val data: List<EncryptedPassportElement>,
    val credentials: EncryptedCredentials,
)
