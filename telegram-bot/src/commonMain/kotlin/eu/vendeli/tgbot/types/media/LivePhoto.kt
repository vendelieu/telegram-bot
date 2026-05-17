package eu.vendeli.tgbot.types.media

import kotlinx.serialization.Serializable

/**
 * This object represents a live photo.
 *
 * [Api reference](https://core.telegram.org/bots/api#livephoto)
 * @property fileId Identifier for the video file which can be used to download or reuse the file
 * @property fileUniqueId Unique identifier for the video file which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
 * @property width Video width as defined by the sender
 * @property height Video height as defined by the sender
 * @property duration Duration of the video in seconds as defined by the sender
 * @property photo Optional. Available sizes of the corresponding static photo
 * @property mimeType Optional. MIME type of the file as defined by the sender
 * @property fileSize Optional. File size in bytes. It can be bigger than 2^31 and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this value.
 */
@Serializable
data class LivePhoto(
    val fileId: String,
    val fileUniqueId: String,
    val width: Int,
    val height: Int,
    val duration: Int,
    val photo: List<PhotoSize>? = null,
    val mimeType: String? = null,
    val fileSize: Long? = null,
)
