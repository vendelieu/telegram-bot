@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.keyboard.MenuButton
import eu.vendeli.tgbot.utils.getReturnType

class SetChatMenuButtonAction(menuButton: MenuButton) : Action<Boolean>, ActionState() {
    override val method: TgMethod = TgMethod("setChatMenuButton")
    override val returnType = getReturnType()

    init {
        parameters["menu_button"] = menuButton
    }
}

fun setChatMenuButton(menuButton: MenuButton) = SetChatMenuButtonAction(menuButton)
