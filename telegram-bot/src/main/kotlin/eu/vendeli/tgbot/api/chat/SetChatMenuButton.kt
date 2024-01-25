@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.keyboard.MenuButton
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class SetChatMenuButtonAction(menuButton: MenuButton) : Action<Boolean>() {
    override val method = TgMethod("setChatMenuButton")
    override val returnType = getReturnType()

    init {
        parameters["menu_button"] = menuButton.toJsonElement()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun setChatMenuButton(menuButton: MenuButton) = SetChatMenuButtonAction(menuButton)
