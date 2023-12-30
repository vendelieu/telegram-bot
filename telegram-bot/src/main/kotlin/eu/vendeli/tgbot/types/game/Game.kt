package eu.vendeli.tgbot.types.game

import eu.vendeli.tgbot.types.MessageEntity
import eu.vendeli.tgbot.types.media.Animation
import eu.vendeli.tgbot.types.media.PhotoSize

data class Game(
    val title: String,
    val description: String,
    val photo: List<PhotoSize>,
    val text: String? = null,
    val textEntities: List<MessageEntity>? = null,
    val animation: Animation? = null,
)
