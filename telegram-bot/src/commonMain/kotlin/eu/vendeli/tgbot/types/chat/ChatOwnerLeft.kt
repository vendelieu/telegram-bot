package eu.vendeli.tgbot.types.chat

import eu.vendeli.tgbot.types.User
import kotlinx.serialization.Serializable

/**
 * Describes a service message about the chat owner leaving the chat.
 *
 * [Api reference](https://core.telegram.org/bots/api#chatownerleft)
 * @property newOwner Optional. The user which will be the new owner of the chat if the previous owner does not return to the chat
 */
@Serializable
data class ChatOwnerLeft(
    val newOwner: User? = null,
)
