package eu.vendeli.tgbot.types

import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

/**
 * This object represents a change of a reaction on a message performed by a user.
 * @property chat The chat containing the message the user reacted to
 * @property messageId Unique identifier of the message inside the chat
 * @property user Optional. The user that changed the reaction, if the user isn't anonymous
 * @property actorChat Optional. The chat on behalf of which the reaction was changed, if the user is anonymous
 * @property date Date of the change in Unix time
 * @property oldReaction Previous list of reaction types that were set by the user
 * @property newReaction New list of reaction types that have been set by the user
 * Api reference: https://core.telegram.org/bots/api#messagereactionupdated
*/
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
