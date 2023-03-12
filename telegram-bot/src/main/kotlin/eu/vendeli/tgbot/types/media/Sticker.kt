package eu.vendeli.tgbot.types.media

import eu.vendeli.tgbot.interfaces.MultipleResponse

enum class StickerFormat {
    Static, Animated, Video;

    override fun toString(): String = name.lowercase()
}

data class Sticker(
    val fileId: String,
    val fileUniqueId: String,
    val width: Int,
    val height: Int,
    val isAnimated: Boolean,
    val isVideo: Boolean,
    val thumb: PhotoSize? = null,
    val emoji: String? = null,
    val setName: String? = null,
    val premiumAnimation: File? = null,
    val maskPosition: MaskPosition? = null,
    val needsRepainting: Boolean? = null,
    val fileSize: Int? = null,
) : MultipleResponse
