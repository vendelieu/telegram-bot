@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class UnbanChatSenderChatAction(senderChatId: Long) : Action<Boolean>() {
    override val method = TgMethod("unbanChatSenderChat")
    override val returnType = getReturnType()

    init {
        parameters["sender_chat_id"] = senderChatId.toJsonElement()
    }
}

/**
 * Use this method to unban a previously banned channel chat in a supergroup or channel. The bot must be an administrator for this to work and must have the appropriate administrator rights. Returns True on success.
 * @param chatId Required 
 * @param senderChatId Required 
 * @returns [Boolean]
 * Api reference: https://core.telegram.org/bots/api#unbanchatsenderchat
*/
@Suppress("NOTHING_TO_INLINE")
inline fun unbanChatSenderChat(senderChatId: Long) = UnbanChatSenderChatAction(senderChatId)

@Suppress("NOTHING_TO_INLINE")
inline fun unbanChatSenderChat(senderChatId: Chat) = unbanChatSenderChat(senderChatId.id)

@Suppress("NOTHING_TO_INLINE")
inline fun unbanChatSenderChat(senderChatId: User) = unbanChatSenderChat(senderChatId.id)
