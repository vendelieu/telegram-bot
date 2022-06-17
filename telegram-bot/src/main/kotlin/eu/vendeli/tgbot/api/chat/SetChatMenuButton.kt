package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.MenuButton
import eu.vendeli.tgbot.types.internal.TgMethod

class SetChatMenuButtonAction(menuButton: MenuButton) : Action<Boolean> {
    override val method: TgMethod = TgMethod("setChatMenuButton")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["menu_button"] = menuButton
    }
}

fun setChatMenuButton(menuButton: MenuButton) = SetChatMenuButtonAction(menuButton)
