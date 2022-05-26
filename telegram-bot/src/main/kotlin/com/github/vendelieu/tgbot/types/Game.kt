package com.github.vendelieu.tgbot.types

data class Game(
    val title: String,
    val description: String,
    val photo: List<PhotoSize>,
    val text: String? = null,
    val textEntities: List<MessageEntity>? = null,
    val animation: Animation?,
)
