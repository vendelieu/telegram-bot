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
