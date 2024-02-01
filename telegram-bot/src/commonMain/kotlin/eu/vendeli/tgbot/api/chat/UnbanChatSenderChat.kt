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

inline fun unbanChatSenderChat(senderChatId: Long) = UnbanChatSenderChatAction(senderChatId)

inline fun unbanChatSenderChat(senderChatId: Chat) = unbanChatSenderChat(senderChatId.id)

inline fun unbanChatSenderChat(senderChatId: User) = unbanChatSenderChat(senderChatId.id)
