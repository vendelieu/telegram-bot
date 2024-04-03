package eu.vendeli.tgbot.types.media

import kotlinx.serialization.Serializable

/**
 * This object represents one size of a photo or a file / sticker thumbnail.
 *
 * Api reference: https://core.telegram.org/bots/api#photosize
 * @property fileId Identifier for this file, which can be used to download or reuse the file
 * @property fileUniqueId Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
 * @property width Photo width
 * @property height Photo height
 * @property fileSize Optional. File size in bytes
 */
@Serializable
data class PhotoSize(
    val fileId: String,
    val fileUniqueId: String,
    val width: Int,
    val height: Int,
    val fileSize: Int? = null,
)
