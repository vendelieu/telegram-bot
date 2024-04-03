@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class UnpinChatMessageAction(messageId: Long) : Action<Boolean>() {
    override val method = TgMethod("unpinChatMessage")
    override val returnType = getReturnType()

    init {
        parameters["message_id"] = messageId.toJsonElement()
    }
}

/**
 * Use this method to remove a message from the list of pinned messages in a chat. If the chat is not a private chat, the bot must be an administrator in the chat for this to work and must have the 'can_pin_messages' administrator right in a supergroup or 'can_edit_messages' administrator right in a channel. Returns True on success.
 *
 * Api reference: https://core.telegram.org/bots/api#unpinchatmessage
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageId Identifier of a message to unpin. If not specified, the most recent pinned message (by sending date) will be unpinned.
 * @returns [Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun unpinChatMessage(messageId: Long) = UnpinChatMessageAction(messageId)
