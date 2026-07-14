package eu.vendeli.tgbot.types.chat

import kotlinx.serialization.Serializable

/**
 * Describes a service message about a chat being added to a community.
 *
 * [Api reference](https://core.telegram.org/bots/api#communitychatadded)
 * @property community The new community to which the chat belongs
 */
@Serializable
data class CommunityChatAdded(
    val community: Community,
)
