package eu.vendeli.tgbot.types

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
    val fileSize: Int?,
)
