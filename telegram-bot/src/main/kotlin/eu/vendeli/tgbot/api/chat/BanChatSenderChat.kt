package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod

class BanChatSenderChatAction(senderChatId: Long) : Action<Boolean> {
    override val method: TgMethod = TgMethod("banChatSenderChat")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["sender_chat_id"] = senderChatId
    }
}

fun banChatSenderChat(senderChatId: Long) = BanChatSenderChatAction(senderChatId)
