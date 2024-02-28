@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class SetChatDescriptionAction(description: String? = null) : Action<Boolean>() {
    override val method = TgMethod("setChatDescription")
    override val returnType = getReturnType()

    init {
        if (description != null) parameters["description"] = description.toJsonElement()
    }
}

/**
 * Use this method to change the description of a group, a supergroup or a channel. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns True on success.
 * @param chatId Required 
 * @param description New chat description, 0-255 characters
 * @returns [Boolean]
 * Api reference: https://core.telegram.org/bots/api#setchatdescription
*/
@Suppress("NOTHING_TO_INLINE")
inline fun setChatDescription(title: String? = null) = SetChatDescriptionAction(title)
