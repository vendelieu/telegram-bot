package eu.vendeli.tgbot.types.chat

import kotlinx.serialization.Serializable

/**
 * Describes a service message about a chat being joined by a user from a community.
 *
 * [Api reference](https://core.telegram.org/bots/api#communitychatjoined)
 * @property community The community from which the chat was joined
 */
@Serializable
data class CommunityChatJoined(
    val community: Community,
)
