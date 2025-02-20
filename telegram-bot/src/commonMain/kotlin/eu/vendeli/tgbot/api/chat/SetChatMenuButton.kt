@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.types.keyboard.MenuButton
import eu.vendeli.tgbot.utils.internal.encodeWith
import eu.vendeli.tgbot.utils.internal.getReturnType

@TgAPI
class SetChatMenuButtonAction(
    menuButton: MenuButton,
) : Action<Boolean>() {
    @TgAPI.Name("setChatMenuButton")
    override val method = "setChatMenuButton"
    override val returnType = getReturnType()

    init {
        parameters["menu_button"] = menuButton.encodeWith(MenuButton.serializer())
    }
}

/**
 * Use this method to change the bot's menu button in a private chat, or the default menu button. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#setchatmenubutton)
 * @param chatId Unique identifier for the target private chat. If not specified, default bot's menu button will be changed
 * @param menuButton A JSON-serialized object for the bot's new menu button. Defaults to MenuButtonDefault
 * @returns [Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun setChatMenuButton(menuButton: MenuButton) = SetChatMenuButtonAction(menuButton)
