package com.github.vendelieu.tgbot.api.chat

import com.github.vendelieu.tgbot.interfaces.Action
import com.github.vendelieu.tgbot.types.internal.TgMethod

class UnpinChatMessageAction(messageId: Long) : Action<Boolean> {
    override val method: TgMethod = TgMethod("unpinChatMessage")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["message_id"] = messageId
    }
}

fun unpinChatMessage(messageId: Long) = UnpinChatMessageAction(messageId)
