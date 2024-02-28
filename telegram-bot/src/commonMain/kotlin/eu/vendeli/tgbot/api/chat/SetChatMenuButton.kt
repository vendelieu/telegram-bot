@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.keyboard.MenuButton
import eu.vendeli.tgbot.utils.encodeWith
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.serde.DynamicLookupSerializer

class SetChatMenuButtonAction(menuButton: MenuButton) : Action<Boolean>() {
    override val method = TgMethod("setChatMenuButton")
    override val returnType = getReturnType()

    init {
        parameters["menu_button"] = menuButton.encodeWith(DynamicLookupSerializer)
    }
}

/**
 * Use this method to change the bot's menu button in a private chat, or the default menu button. Returns True on success.
 * @param chatId Unique identifier for the target private chat. If not specified, default bot's menu button will be changed
 * @param menuButton A JSON-serialized object for the bot's new menu button. Defaults to MenuButtonDefault
 * @returns [Boolean]
 * Api reference: https://core.telegram.org/bots/api#setchatmenubutton
*/
@Suppress("NOTHING_TO_INLINE")
inline fun setChatMenuButton(menuButton: MenuButton) = SetChatMenuButtonAction(menuButton)
