package eu.vendeli.tgbot.types

import eu.vendeli.tgbot.types.chat.Chat
import java.time.Instant

data class MessageReactionUpdated(
    val chat: Chat,
    val messageId: Long,
    val user: User? = null,
    val actorChat: Chat? = null,
    val date: Instant,
    val oldReaction: List<ReactionType>,
    val newReaction: List<ReactionType>,
)
