package eu.vendeli.tgbot.types

import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class MessageReactionCountUpdated(
    val chat: Chat,
    val messageId: Long,
    @Serializable(InstantSerializer::class)
    val date: Instant,
    val reactions: List<ReactionCount>,
)
