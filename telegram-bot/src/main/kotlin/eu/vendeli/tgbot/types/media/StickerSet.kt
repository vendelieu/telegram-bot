package eu.vendeli.tgbot.types.media

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Type of sticker
 *
 */
@Serializable
enum class StickerType {
    @SerialName("regular")
    Regular,

    @SerialName("mask")
    Mask,

    @SerialName("custom_emoji")
    CustomEmoji,
}

@Serializable
data class StickerSet(
    val name: String,
    val title: String,
    val stickerType: StickerType,
    val isAnimated: Boolean,
    val isVideo: Boolean,
    val stickers: List<Sticker>,
    val thumbnail: PhotoSize? = null,
)
