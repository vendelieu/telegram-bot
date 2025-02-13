package eu.vendeli.tgbot.types.media

import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

/**
 * This object represents a video file.
 *
 * [Api reference](https://core.telegram.org/bots/api#video)
 * @property fileId Identifier for this file, which can be used to download or reuse the file
 * @property fileUniqueId Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
 * @property width Video width as defined by the sender
 * @property height Video height as defined by the sender
 * @property duration Duration of the video in seconds as defined by the sender
 * @property thumbnail Optional. Video thumbnail
 * @property cover Optional. Available sizes of the cover of the video in the message
 * @property startTimestamp Optional. Timestamp in seconds from which the video will play in the message
 * @property fileName Optional. Original filename as defined by the sender
 * @property mimeType Optional. MIME type of the file as defined by the sender
 * @property fileSize Optional. File size in bytes. It can be bigger than 2^31 and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this value.
 */
@Serializable
data class Video(
    val fileId: String,
    val fileUniqueId: String,
    val width: Int,
    val height: Int,
    val duration: Int,
    val thumbnail: PhotoSize? = null,
    val cover: ImplicitFile? = null,
    @Serializable(InstantSerializer::class)
    val startTimestamp: Instant? = null,
    val fileName: String? = null,
    val mimeType: String? = null,
    val fileSize: Long? = null,
)
