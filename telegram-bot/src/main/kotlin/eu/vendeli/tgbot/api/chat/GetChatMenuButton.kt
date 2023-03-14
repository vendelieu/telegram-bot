@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.keyboard.MenuButton
import eu.vendeli.tgbot.utils.getReturnType

class GetChatMenuButtonAction : Action<MenuButton>, ActionState() {
    override val TgAction<MenuButton>.method: TgMethod
        get() = TgMethod("getChatMenuButton")
    override val TgAction<MenuButton>.returnType: Class<MenuButton>
        get() = getReturnType()
}

fun getChatMenuButton() = GetChatMenuButtonAction()
