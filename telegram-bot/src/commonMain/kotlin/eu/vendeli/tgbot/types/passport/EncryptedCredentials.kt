package eu.vendeli.tgbot.types.passport

import kotlinx.serialization.Serializable

/**
 * Describes data required for decrypting and authenticating EncryptedPassportElement. See the Telegram Passport Documentation for a complete description of the data decryption and authentication processes.
 * @property data Base64-encoded encrypted JSON-serialized data with unique user's payload, data hashes and secrets required for EncryptedPassportElement decryption and authentication
 * @property hash Base64-encoded data hash for data authentication
 * @property secret Base64-encoded secret, encrypted with the bot's public RSA key, required for data decryption
 * Api reference: https://core.telegram.org/bots/api#encryptedcredentials
*/
@Serializable
data class EncryptedCredentials(
    val data: String,
    val hash: String,
    val secret: String,
)
