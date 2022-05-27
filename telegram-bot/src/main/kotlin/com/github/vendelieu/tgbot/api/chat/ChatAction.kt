package com.github.vendelieu.tgbot.api.chat

import com.github.vendelieu.tgbot.interfaces.Action
import com.github.vendelieu.tgbot.types.ChatAction
import com.github.vendelieu.tgbot.types.internal.TgMethod

class SendChatAction(action: ChatAction) : Action<Boolean> {
    override val method: TgMethod = TgMethod("sendChatAction")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["action"] = action
    }
}

fun chatAction(block: () -> ChatAction) = SendChatAction(block())
fun chatAction(action: ChatAction) = SendChatAction(action)
