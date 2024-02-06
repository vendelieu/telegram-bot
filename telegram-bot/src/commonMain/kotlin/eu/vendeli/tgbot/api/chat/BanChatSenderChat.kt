@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class BanChatSenderChatAction(senderChatId: Long) : Action<Boolean>() {
    override val method = TgMethod("banChatSenderChat")
    override val returnType = getReturnType()

    init {
        parameters["sender_chat_id"] = senderChatId.toJsonElement()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun banChatSenderChat(senderChatId: Long) = BanChatSenderChatAction(senderChatId)

@Suppress("NOTHING_TO_INLINE")
inline fun banChatSenderChat(senderChatId: Chat) = banChatSenderChat(senderChatId.id)

@Suppress("NOTHING_TO_INLINE")
inline fun banChatSenderChat(senderChatId: User) = banChatSenderChat(senderChatId.id)
