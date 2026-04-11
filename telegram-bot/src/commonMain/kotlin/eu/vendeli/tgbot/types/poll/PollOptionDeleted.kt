package eu.vendeli.tgbot.types.poll

import eu.vendeli.tgbot.types.msg.MaybeInaccessibleMessage
import eu.vendeli.tgbot.types.msg.MessageEntity
import kotlinx.serialization.Serializable

/**
 * Describes a service message about an option deleted from a poll.
 *
 * [Api reference](https://core.telegram.org/bots/api#polloptiondeleted)
 * @property pollMessage Optional. Message containing the poll from which the option was deleted, if known. Note that the Message object in this field will not contain the reply_to_message field even if it itself is a reply.
 * @property optionPersistentId Unique identifier of the deleted option
 * @property optionText Option text
 * @property optionTextEntities Optional. Special entities that appear in the option_text
 */
@Serializable
data class PollOptionDeleted(
    val optionPersistentId: String,
    val optionText: String,
    val pollMessage: MaybeInaccessibleMessage? = null,
    val optionTextEntities: List<MessageEntity>? = null,
)
