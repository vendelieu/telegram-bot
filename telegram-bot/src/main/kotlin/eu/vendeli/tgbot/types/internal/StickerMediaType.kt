package eu.vendeli.tgbot.types.internal

@Deprecated("Use sticker file direct sending.")
sealed class StickerMediaType {
    object Png : StickerMediaType()
    object Tgs : StickerMediaType()
    object Webm : StickerMediaType()
}
