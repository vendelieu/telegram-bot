package com.github.vendelieu.tgbot.api.chat

import com.github.vendelieu.tgbot.interfaces.Action
import com.github.vendelieu.tgbot.types.internal.TgMethod

class UnbanChatSenderChatAction(senderChatId: Long) : Action<Boolean> {
    override val method: TgMethod = TgMethod("unbanChatSenderChat")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["sender_chat_id"] = senderChatId
    }
}

fun unbanChatSenderChat(senderChatId: Long) = UnbanChatSenderChatAction(senderChatId)
