package eu.vendeli.tgbot.types.media

import kotlinx.serialization.Serializable

/**
 * This object represents a video message (available in Telegram apps as of v.4.0).
 *
 * [Api reference](https://core.telegram.org/bots/api#videonote)
 * @property fileId Identifier for this file, which can be used to download or reuse the file
 * @property fileUniqueId Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
 * @property length Video width and height (diameter of the video message) as defined by sender
 * @property duration Duration of the video in seconds as defined by sender
 * @property thumbnail Optional. Video thumbnail
 * @property fileSize Optional. File size in bytes
 */
@Serializable
data class VideoNote(
    val fileId: String,
    val fileUniqueId: String,
    val length: Int,
    val duration: Int,
    val thumbnail: PhotoSize? = null,
    val fileSize: Int? = null,
)
