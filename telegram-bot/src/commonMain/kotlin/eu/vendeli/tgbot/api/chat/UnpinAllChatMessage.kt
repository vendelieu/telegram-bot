@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class UnpinAllChatMessagesAction : Action<Boolean>() {
    override val method = TgMethod("unpinAllChatMessages")
    override val returnType = getReturnType()
}

/**
 * Use this method to clear the list of pinned messages in a chat. If the chat is not a private chat, the bot must be an administrator in the chat for this to work and must have the 'can_pin_messages' administrator right in a supergroup or 'can_edit_messages' administrator right in a channel. Returns True on success.
 * Api reference: https://core.telegram.org/bots/api#unpinallchatmessages
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @returns [Boolean]
*/
@Suppress("NOTHING_TO_INLINE")
inline fun unpinAllChatMessages() = UnpinAllChatMessagesAction()
