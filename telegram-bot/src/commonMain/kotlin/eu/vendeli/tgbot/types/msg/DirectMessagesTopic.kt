package eu.vendeli.tgbot.types.msg

import eu.vendeli.tgbot.types.User
import kotlinx.serialization.Serializable

/**
 * Describes a topic of a direct messages chat.
 *
 * [Api reference](https://core.telegram.org/bots/api#directmessagestopic)
 * @property topicId Unique identifier of the topic
 * @property user Optional. Information about the user that created the topic. Currently, it is always present
 */
@Serializable
data class DirectMessagesTopic(
    val topicId: Int,
    val user: User? = null,
)
