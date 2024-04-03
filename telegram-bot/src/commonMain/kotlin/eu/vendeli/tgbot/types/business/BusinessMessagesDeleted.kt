package eu.vendeli.tgbot.types.business

import eu.vendeli.tgbot.types.chat.Chat
import kotlinx.serialization.Serializable

/**
 * This object is received when messages are deleted from a connected business account.
 *
 * Api reference: https://core.telegram.org/bots/api#businessmessagesdeleted
 * @property businessConnectionId Unique identifier of the business connection
 * @property chat Information about a chat in the business account. The bot may not have access to the chat or the corresponding user.
 * @property messageIds A JSON-serialized list of identifiers of deleted messages in the chat of the business account
 */
@Serializable
data class BusinessMessagesDeleted(
    val businessConnectionId: String,
    val chat: Chat,
    val messageIds: List<Int>,
)
