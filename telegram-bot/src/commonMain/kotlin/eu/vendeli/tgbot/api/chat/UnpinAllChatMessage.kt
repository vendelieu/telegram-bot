@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.utils.internal.getReturnType

@TgAPI
class UnpinAllChatMessagesAction : Action<Boolean>() {
    @TgAPI.Name("unpinAllChatMessages")
    override val method = "unpinAllChatMessages"
    override val returnType = getReturnType()
}

/**
 * Use this method to clear the list of pinned messages in a chat. In private chats and channel direct messages chats, no additional rights are required to unpin all pinned messages. Conversely, the bot must be an administrator with the 'can_pin_messages' right or the 'can_edit_messages' right to unpin all pinned messages in groups and channels respectively. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#unpinallchatmessages)
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @returns [Boolean]
 */
@TgAPI
inline fun unpinAllChatMessages() = UnpinAllChatMessagesAction()
