package com.github.vendelieu.tgbot.api.chat

import com.github.vendelieu.tgbot.interfaces.Action
import com.github.vendelieu.tgbot.types.internal.TgMethod

class GetChatMemberCountAction : Action<Int> {
    override val method: TgMethod = TgMethod("getChatMemberCount")
    override val parameters: MutableMap<String, Any> = mutableMapOf()
}

fun getChatMemberCount() = GetChatMemberCountAction()
