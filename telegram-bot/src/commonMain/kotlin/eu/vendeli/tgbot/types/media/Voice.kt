package eu.vendeli.tgbot.types.media

import kotlinx.serialization.Serializable

/**
 * This object represents a voice note.
 *
 * [Api reference](https://core.telegram.org/bots/api#voice)
 * @property fileId Identifier for this file, which can be used to download or reuse the file
 * @property fileUniqueId Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
 * @property duration Duration of the audio in seconds as defined by the sender
 * @property mimeType Optional. MIME type of the file as defined by the sender
 * @property fileSize Optional. File size in bytes. It can be bigger than 2^31 and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this value.
 */
@Serializable
data class Voice(
    val fileId: String,
    val fileUniqueId: String,
    val duration: Int,
    val mimeType: String? = null,
    val fileSize: Long? = null,
)
