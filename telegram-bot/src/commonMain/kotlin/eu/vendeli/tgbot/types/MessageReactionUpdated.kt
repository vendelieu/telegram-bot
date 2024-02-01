package eu.vendeli.tgbot.types

import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class MessageReactionUpdated(
    val chat: Chat,
    val messageId: Long,
    val user: User? = null,
    val actorChat: Chat? = null,
    @Serializable(InstantSerializer::class)
    val date: Instant,
    val oldReaction: List<ReactionType>,
    val newReaction: List<ReactionType>,
)
