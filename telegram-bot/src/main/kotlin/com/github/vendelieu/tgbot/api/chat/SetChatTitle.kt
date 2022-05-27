package com.github.vendelieu.tgbot.api.chat

import com.github.vendelieu.tgbot.interfaces.Action
import com.github.vendelieu.tgbot.types.internal.TgMethod

class SetChatTitleAction(title: String) : Action<Boolean> {
    override val method: TgMethod = TgMethod("setChatTitle")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["title"] = title
    }
}

fun setChatTitle(title: String) = SetChatTitleAction(title)
