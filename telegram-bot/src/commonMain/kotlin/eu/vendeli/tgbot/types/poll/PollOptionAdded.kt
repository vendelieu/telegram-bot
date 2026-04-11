package eu.vendeli.tgbot.types.poll

import eu.vendeli.tgbot.types.msg.MaybeInaccessibleMessage
import eu.vendeli.tgbot.types.msg.MessageEntity
import kotlinx.serialization.Serializable

/**
 * Describes a service message about an option added to a poll.
 *
 * [Api reference](https://core.telegram.org/bots/api#polloptionadded)
 * @property pollMessage Optional. Message containing the poll to which the option was added, if known. Note that the Message object in this field will not contain the reply_to_message field even if it itself is a reply.
 * @property optionPersistentId Unique identifier of the added option
 * @property optionText Option text
 * @property optionTextEntities Optional. Special entities that appear in the option_text
 */
@Serializable
data class PollOptionAdded(
    val optionPersistentId: String,
    val optionText: String,
    val pollMessage: MaybeInaccessibleMessage? = null,
    val optionTextEntities: List<MessageEntity>? = null,
)
