package com.github.vendelieu.tgbot.types

data class Voice(
    val fileId: String,
    val fileUniqueId: String,
    val duration: Int,
    val mimeType: Sticker? = null,
    val fileSize: Int?,
)
