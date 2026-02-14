package eu.vendeli.tgbot.types.chat

import eu.vendeli.tgbot.types.User
import kotlinx.serialization.Serializable

/**
 * Describes a service message about an ownership change in the chat.
 *
 * [Api reference](https://core.telegram.org/bots/api#chatownerchanged)
 * @property newOwner The new owner of the chat
 */
@Serializable
data class ChatOwnerChanged(
    val newOwner: User,
)
