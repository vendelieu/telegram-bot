package com.github.vendelieu.tgbot.types

data class StickerSet(
    val name: String,
    val title: String,
    val isAnimated: Boolean,
    val isVideo: Boolean,
    val containMasks: Boolean,
    val stickers: List<Sticker>,
    val thumb: PhotoSize? = null
)
