@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.keyboard.MenuButton
import eu.vendeli.tgbot.utils.getReturnType

class GetChatMenuButtonAction : Action<MenuButton>() {
    override val method = TgMethod("getChatMenuButton")
    override val returnType = getReturnType()
}

inline fun getChatMenuButton() = GetChatMenuButtonAction()
