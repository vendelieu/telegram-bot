package com.github.vendelieu.tgbot.api

import com.github.vendelieu.tgbot.interfaces.Action
import com.github.vendelieu.tgbot.types.internal.TgMethod

class DeleteMessageAction(messageId: Long) : Action<Boolean> {
    override val method: TgMethod = TgMethod("deleteMessage")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["message_id"] = messageId
    }
}

fun deleteMessage(messageId: Long) = DeleteMessageAction(messageId)
