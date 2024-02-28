package eu.vendeli.tgbot.types.media

import eu.vendeli.tgbot.types.chat.Chat
import kotlinx.serialization.Serializable

@Serializable
data class Story(
    val id: Int,
    val chat: Chat,
)
