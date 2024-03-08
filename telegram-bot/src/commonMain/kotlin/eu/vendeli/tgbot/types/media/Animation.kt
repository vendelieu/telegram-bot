package eu.vendeli.tgbot.types.media

import kotlinx.serialization.Serializable

/**
 * This object represents an animation file (GIF or H.264/MPEG-4 AVC video without sound).
 * Api reference: https://core.telegram.org/bots/api#animation
 * @property fileId Identifier for this file, which can be used to download or reuse the file
 * @property fileUniqueId Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
 * @property width Video width as defined by sender
 * @property height Video height as defined by sender
 * @property duration Duration of the video in seconds as defined by sender
 * @property thumbnail Optional. Animation thumbnail as defined by sender
 * @property fileName Optional. Original animation filename as defined by sender
 * @property mimeType Optional. MIME type of the file as defined by sender
 * @property fileSize Optional. File size in bytes. It can be bigger than 2^31 and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this value.
*/
@Serializable
data class Animation(
    val fileId: String,
    val fileUniqueId: String,
    val width: Int,
    val height: Int,
    val duration: Int,
    val thumbnail: PhotoSize? = null,
    val fileName: String? = null,
    val mimeType: String? = null,
    val fileSize: Long? = null,
)
