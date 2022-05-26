package com.github.vendelieu.tgbot.types

data class File(
    val fileId: String,
    val fileUniqueId: String,
    val fileSize: Int? = null,
    val filePath: String? = null,
)
