@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class BanChatSenderChatAction(senderChatId: Long) : Action<Boolean>() {
    override val method = TgMethod("banChatSenderChat")
    override val returnType = getReturnType()

    init {
        parameters["sender_chat_id"] = senderChatId
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun banChatSenderChat(senderChatId: Long) = BanChatSenderChatAction(senderChatId)
