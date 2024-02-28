@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class SetChatTitleAction(title: String) : Action<Boolean>() {
    override val method = TgMethod("setChatTitle")
    override val returnType = getReturnType()

    init {
        parameters["title"] = title.toJsonElement()
    }
}

/**
 * Use this method to change the title of a chat. Titles can't be changed for private chats. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns True on success.
 * @param chatId Required 
 * @param title Required 
 * @returns [Boolean]
 * Api reference: https://core.telegram.org/bots/api#setchattitle
*/
@Suppress("NOTHING_TO_INLINE")
inline fun setChatTitle(title: String) = SetChatTitleAction(title)
