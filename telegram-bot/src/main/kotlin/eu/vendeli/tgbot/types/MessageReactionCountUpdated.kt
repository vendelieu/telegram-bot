package eu.vendeli.tgbot.types

import eu.vendeli.tgbot.types.chat.Chat
import java.time.Instant

data class MessageReactionCountUpdated(
    val chat: Chat,
    val messageId: Long,
    val date: Instant,
    val reactions: List<ReactionCount>,
)
