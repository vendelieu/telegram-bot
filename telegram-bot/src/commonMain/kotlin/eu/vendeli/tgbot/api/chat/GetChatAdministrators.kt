@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.chat.ChatMember
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class GetChatAdministratorsAction : Action<List<ChatMember>>() {
    override val method = TgMethod("getChatAdministrators")
    override val returnType = getReturnType()
}

/**
 * Use this method to get a list of administrators in a chat, which aren't bots. Returns an Array of ChatMember objects.
 * @param chatId Required 
 * @returns [Array of ChatMember]
 * Api reference: https://core.telegram.org/bots/api#getchatadministrators
*/
@Suppress("NOTHING_TO_INLINE")
inline fun getChatAdministrators() = GetChatAdministratorsAction()
