package eu.vendeli.tgbot.types.poll

import eu.vendeli.tgbot.types.msg.MessageEntity
import kotlinx.serialization.Serializable

/**
 * This object contains information about one answer option in a poll.
 *
 * [Api reference](https://core.telegram.org/bots/api#polloption)
 * @property text Option text, 1-100 characters
 * @property textEntities Optional. Special entities that appear in the option text. Currently, only custom emoji entities are allowed in poll option texts
 * @property voterCount Number of users that voted for this option
 */
@Serializable
data class PollOption(
    val text: String,
    val voterCount: Int,
    val textEntities: List<MessageEntity>? = null,
)
