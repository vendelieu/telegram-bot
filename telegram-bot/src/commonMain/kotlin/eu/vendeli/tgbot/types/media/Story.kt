package eu.vendeli.tgbot.types.media

import eu.vendeli.tgbot.types.chat.Chat
import kotlinx.serialization.Serializable

/**
 * This object represents a story.
 * @property chat Chat that posted the story
 * @property id Unique identifier for the story in the chat
 * Api reference: https://core.telegram.org/bots/api#story
*/
@Serializable
data class Story(
    val id: Int,
    val chat: Chat,
)
