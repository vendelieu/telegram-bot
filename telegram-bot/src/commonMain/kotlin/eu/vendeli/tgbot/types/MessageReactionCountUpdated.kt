package eu.vendeli.tgbot.types

import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

/**
 * This object represents reaction changes on a message with anonymous reactions.
 *
 * [Api reference](https://core.telegram.org/bots/api#messagereactioncountupdated)
 * @property chat The chat containing the message
 * @property messageId Unique message identifier inside the chat
 * @property date Date of the change in Unix time
 * @property reactions List of reactions that are present on the message
 */
@Serializable
data class MessageReactionCountUpdated(
    val chat: Chat,
    val messageId: Long,
    @Serializable(InstantSerializer::class)
    val date: Instant,
    val reactions: List<ReactionCount>,
)
