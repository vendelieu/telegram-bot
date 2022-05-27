package com.github.vendelieu.tgbot.api.chat

import com.github.vendelieu.tgbot.interfaces.Action
import com.github.vendelieu.tgbot.types.MenuButton
import com.github.vendelieu.tgbot.types.internal.TgMethod

class SetChatMenuButtonAction(menuButton: MenuButton) : Action<Boolean> {
    override val method: TgMethod = TgMethod("setChatMenuButton")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["menu_button"] = menuButton
    }
}

fun setChatMenuButton(menuButton: MenuButton) = SetChatMenuButtonAction(menuButton)
