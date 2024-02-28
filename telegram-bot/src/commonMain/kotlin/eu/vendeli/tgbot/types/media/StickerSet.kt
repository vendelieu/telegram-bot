package eu.vendeli.tgbot.types.media

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class StickerType {
    @SerialName("regular")
    Regular,

    @SerialName("mask")
    Mask,

    @SerialName("custom_emoji")
    CustomEmoji,
}

/**
 * This object represents a sticker set.
 * @property name Sticker set name
 * @property title Sticker set title
 * @property stickerType Type of stickers in the set, currently one of "regular", "mask", "custom_emoji"
 * @property isAnimated True, if the sticker set contains animated stickers
 * @property isVideo True, if the sticker set contains video stickers
 * @property stickers List of all set stickers
 * @property thumbnail Optional. Sticker set thumbnail in the .WEBP, .TGS, or .WEBM format
 * Api reference: https://core.telegram.org/bots/api#stickerset
*/
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
