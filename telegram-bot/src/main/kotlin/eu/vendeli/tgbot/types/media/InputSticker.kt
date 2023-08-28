package eu.vendeli.tgbot.types.media

import eu.vendeli.tgbot.types.internal.StickerFile

data class InputSticker(
    var sticker: StickerFile,
    val emojiList: List<String>,
    val maskPosition: MaskPosition? = null,
    val keywords: List<String>? = null,
)
