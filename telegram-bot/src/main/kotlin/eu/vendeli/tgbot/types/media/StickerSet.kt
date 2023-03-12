package eu.vendeli.tgbot.types.media

/**
 * Type of sticker
 *
 * @property literal
 */
enum class StickerType(private val literal: String) {
    Regular("regular"), Mask("mask"), CustomEmoji("custom_emoji");

    override fun toString(): String = literal
}

data class StickerSet(
    val name: String,
    val title: String,
    val stickerType: StickerType,
    val isAnimated: Boolean,
    val isVideo: Boolean,
    val stickers: List<Sticker>,
    val thumb: PhotoSize? = null,
)
