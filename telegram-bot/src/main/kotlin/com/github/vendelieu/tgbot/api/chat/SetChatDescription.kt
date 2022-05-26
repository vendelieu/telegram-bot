package com.github.vendelieu.tgbot.api.chat

import com.github.vendelieu.tgbot.interfaces.Action
import com.github.vendelieu.tgbot.types.internal.TgMethod

class SetChatDescriptionAction(description: String) : Action<Boolean> {
    override val method: TgMethod = TgMethod("setChatDescription")
    override val parameters: MutableMap<String, Any> = mutableMapOf()

    init {
        parameters["description"] = description
    }
}

fun setChatDescription(title: String) = SetChatDescriptionAction(title)
