package com.github.vendelieu.tgbot.api.chat

import com.github.vendelieu.tgbot.interfaces.Action
import com.github.vendelieu.tgbot.types.internal.TgMethod

class LeaveChatAction : Action<Boolean> {
    override val method: TgMethod = TgMethod("leaveChat")
    override val parameters: MutableMap<String, Any> = mutableMapOf()
}

fun leaveChat() = LeaveChatAction()
