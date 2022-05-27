package com.github.vendelieu.tgbot.api.chat

import com.github.vendelieu.tgbot.interfaces.Action
import com.github.vendelieu.tgbot.types.internal.TgMethod

class UnpinAllChatMessageAction : Action<Boolean> {
    override val method: TgMethod = TgMethod("unpinAllChatMessages")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()
}

fun unpinAllChatMessage() = UnpinAllChatMessageAction()
