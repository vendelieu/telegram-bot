package com.github.vendelieu.tgbot.api.chat

import com.github.vendelieu.tgbot.interfaces.Action
import com.github.vendelieu.tgbot.types.Chat
import com.github.vendelieu.tgbot.types.internal.TgMethod

class GetChatAction : Action<Chat> {
    override val method: TgMethod = TgMethod("getChat")
    override val parameters: MutableMap<String, Any> = mutableMapOf()
}

fun getChat() = GetChatAction()
