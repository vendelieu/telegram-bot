@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.interfaces.action.BusinessActionExt
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class UnpinChatMessageAction(
    messageId: Long,
) : Action<Boolean>(),
    BusinessActionExt<Boolean> {
    @TgAPI.Name("unpinChatMessage")
    override val method = "unpinChatMessage"
    override val returnType = getReturnType()

    init {
        parameters["message_id"] = messageId.toJsonElement()
    }
}

/**
 * Use this method to remove a message from the list of pinned messages in a chat. In private chats and channel direct messages chats, all messages can be unpinned. Conversely, the bot must be an administrator with the 'can_pin_messages' right or the 'can_edit_messages' right to unpin messages in groups and channels respectively. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#unpinchatmessage)
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be unpinned
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageId Identifier of the message to unpin. Required if business_connection_id is specified. If not specified, the most recent pinned message (by sending date) will be unpinned.
 * @returns [Boolean]
 */
@TgAPI
inline fun unpinChatMessage(messageId: Long) = UnpinChatMessageAction(messageId)
