@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.MenuButton
import eu.vendeli.tgbot.types.internal.TgMethod

class GetChatMenuButtonAction : Action<MenuButton> {
    override val method: TgMethod = TgMethod("getChatMenuButton")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()
}

fun getChatMenuButton() = GetChatMenuButtonAction()
