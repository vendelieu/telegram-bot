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

/**
 * Use this method to get the current value of the bot's menu button in a private chat, or the default menu button. Returns MenuButton on success.
 * @param chatId Unique identifier for the target private chat. If not specified, default bot's menu button will be returned
 * @returns [MenuButton]
 * Api reference: https://core.telegram.org/bots/api#getchatmenubutton
*/
@Suppress("NOTHING_TO_INLINE")
inline fun getChatMenuButton() = GetChatMenuButtonAction()
