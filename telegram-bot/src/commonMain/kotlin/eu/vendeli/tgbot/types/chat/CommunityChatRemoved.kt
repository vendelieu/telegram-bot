package eu.vendeli.tgbot.types.chat

import kotlinx.serialization.Serializable

/**
 * This object represents a service message about a chat removed from a community. Currently holds no information.
 *
 * [Api reference](https://core.telegram.org/bots/api#communitychatremoved)
 */
@Serializable
data object CommunityChatRemoved
