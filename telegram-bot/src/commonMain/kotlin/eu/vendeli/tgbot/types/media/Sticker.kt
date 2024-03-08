package eu.vendeli.tgbot.types.media

import eu.vendeli.tgbot.interfaces.MultipleResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class StickerFormat {
    @SerialName("static")
    Static,

    @SerialName("animated")
    Animated,

    @SerialName("video")
    Video,
}

/**
 * This object represents a sticker.
 * Api reference: https://core.telegram.org/bots/api#sticker
 * @property fileId Identifier for this file, which can be used to download or reuse the file
 * @property fileUniqueId Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
 * @property type Type of the sticker, currently one of "regular", "mask", "custom_emoji". The type of the sticker is independent from its format, which is determined by the fields is_animated and is_video.
 * @property width Sticker width
 * @property height Sticker height
 * @property isAnimated True, if the sticker is animated
 * @property isVideo True, if the sticker is a video sticker
 * @property thumbnail Optional. Sticker thumbnail in the .WEBP or .JPG format
 * @property emoji Optional. Emoji associated with the sticker
 * @property setName Optional. Name of the sticker set to which the sticker belongs
 * @property premiumAnimation Optional. For premium regular stickers, premium animation for the sticker
 * @property maskPosition Optional. For mask stickers, the position where the mask should be placed
 * @property customEmojiId Optional. For custom emoji stickers, unique identifier of the custom emoji
 * @property needsRepainting Optional. True, if the sticker must be repainted to a text color in messages, the color of the Telegram Premium badge in emoji status, white color on chat photos, or another appropriate color in other places
 * @property fileSize Optional. File size in bytes
*/
@Serializable
data class Sticker(
    val fileId: String,
    val fileUniqueId: String,
    val type: StickerType,
    val width: Int,
    val height: Int,
    val isAnimated: Boolean,
    val isVideo: Boolean,
    val thumbnail: PhotoSize? = null,
    val emoji: String? = null,
    val setName: String? = null,
    val premiumAnimation: File? = null,
    val maskPosition: MaskPosition? = null,
    val customEmojiId: String? = null,
    val needsRepainting: Boolean? = null,
    val fileSize: Int? = null,
) : MultipleResponse
