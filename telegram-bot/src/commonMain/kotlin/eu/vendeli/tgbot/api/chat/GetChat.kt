@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class GetChatAction : Action<Chat>() {
    override val method = TgMethod("getChat")
    override val returnType = getReturnType()
}

/**
 * Use this method to get up to date information about the chat. Returns a Chat object on success.
 * Api reference: https://core.telegram.org/bots/api#getchat
 * @param chatId Unique identifier for the target chat or username of the target supergroup or channel (in the format @channelusername)
 * @returns [Chat]
*/
@Suppress("NOTHING_TO_INLINE")
inline fun getChat() = GetChatAction()
