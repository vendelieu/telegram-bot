package eu.vendeli.tgbot.types.msg

import eu.vendeli.tgbot.types.User
import kotlinx.serialization.Serializable

/**
 * Describes a topic of a direct messages chat.
 *
 * [Api reference](https://core.telegram.org/bots/api#directmessagestopic)
 * @property topicId Unique identifier of the topic. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a 64-bit integer or double-precision float type are safe for storing this identifier.
 * @property user Optional. Information about the user that created the topic. Currently, it is always present
 */
@Serializable
data class DirectMessagesTopic(
    val topicId: Int,
    val user: User? = null,
)
