package eu.vendeli.tgbot.types.media

import kotlinx.serialization.Serializable

/**
 * This object represents a video file of a specific quality.
 *
 * [Api reference](https://core.telegram.org/bots/api#videoquality)
 * @property fileId Identifier for this file, which can be used to download or reuse the file
 * @property fileUniqueId Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
 * @property width Video width
 * @property height Video height
 * @property codec Codec that was used to encode the video, for example, "h264", "h265", or "av01"
 * @property fileSize Optional. File size in bytes. It can be bigger than 2^31 and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this value.
 */
@Serializable
data class VideoQuality(
    val fileId: String,
    val fileUniqueId: String,
    val width: Int,
    val height: Int,
    val codec: String,
    val fileSize: Long? = null,
)
