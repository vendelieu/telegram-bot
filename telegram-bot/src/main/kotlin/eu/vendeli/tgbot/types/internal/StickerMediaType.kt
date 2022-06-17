package eu.vendeli.tgbot.types.internal

sealed class StickerMediaType {
    object Png : StickerMediaType()
    object Tgs : StickerMediaType()
    object Webm : StickerMediaType()
}
