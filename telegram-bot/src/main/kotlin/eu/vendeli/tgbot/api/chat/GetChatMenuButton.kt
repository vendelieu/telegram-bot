@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.types.MenuButton
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class GetChatMenuButtonAction : Action<MenuButton>, ActionState() {
    override val method: TgMethod = TgMethod("getChatMenuButton")
    override val returnType = getReturnType()
}

fun getChatMenuButton() = GetChatMenuButtonAction()
