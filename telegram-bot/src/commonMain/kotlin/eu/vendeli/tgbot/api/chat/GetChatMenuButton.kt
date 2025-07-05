@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.types.keyboard.MenuButton
import eu.vendeli.tgbot.utils.internal.getReturnType

@TgAPI
class GetChatMenuButtonAction : Action<MenuButton>() {
    @TgAPI.Name("getChatMenuButton")
    override val method = "getChatMenuButton"
    override val returnType = getReturnType()
}

/**
 * Use this method to get the current value of the bot's menu button in a private chat, or the default menu button. Returns MenuButton on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#getchatmenubutton)
 * @param chatId Unique identifier for the target private chat. If not specified, default bot's menu button will be returned
 * @returns [MenuButton]
 */
@TgAPI
inline fun getChatMenuButton() = GetChatMenuButtonAction()
