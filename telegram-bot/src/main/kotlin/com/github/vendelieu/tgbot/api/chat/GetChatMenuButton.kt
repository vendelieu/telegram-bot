package com.github.vendelieu.tgbot.api.chat

import com.github.vendelieu.tgbot.interfaces.Action
import com.github.vendelieu.tgbot.types.MenuButton
import com.github.vendelieu.tgbot.types.internal.TgMethod

class GetChatMenuButtonAction : Action<MenuButton> {
    override val method: TgMethod = TgMethod("getChatMenuButton")
    override val parameters: MutableMap<String, Any> = mutableMapOf()
}

fun getChatMenuButton() = GetChatMenuButtonAction()
