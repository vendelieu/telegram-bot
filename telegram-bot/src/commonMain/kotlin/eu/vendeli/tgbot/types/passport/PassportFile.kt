package eu.vendeli.tgbot.types.passport

import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

/**
 * This object represents a file uploaded to Telegram Passport. Currently all Telegram Passport files are in JPEG format when decrypted and don't exceed 10MB.
 *
 * [Api reference](https://core.telegram.org/bots/api#passportfile)
 * @property fileId Identifier for this file, which can be used to download or reuse the file
 * @property fileUniqueId Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
 * @property fileSize File size in bytes
 * @property fileDate Unix time when the file was uploaded
 */
@Serializable
data class PassportFile(
    val fileId: String,
    val fileUniqueId: String,
    val fileSize: Int,
    @Serializable(InstantSerializer::class)
    val fileDate: Instant,
)
