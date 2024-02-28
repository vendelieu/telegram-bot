@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class SetChatAdministratorCustomTitleAction(userId: Long, customTitle: String) : Action<Boolean>() {
    override val method = TgMethod("setChatAdministratorCustomTitle")
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId.toJsonElement()
        parameters["custom_title"] = customTitle.toJsonElement()
    }
}

/**
 * Use this method to set a custom title for an administrator in a supergroup promoted by the bot. Returns True on success.
 * @param chatId Required 
 * @param userId Required 
 * @param customTitle Required 
 * @returns [Boolean]
 * Api reference: https://core.telegram.org/bots/api#setchatadministratorcustomtitle
*/
@Suppress("NOTHING_TO_INLINE")
inline fun setChatAdministratorCustomTitle(userId: Long, customTitle: String) =
    SetChatAdministratorCustomTitleAction(userId, customTitle)

@Suppress("NOTHING_TO_INLINE")
inline fun setChatAdministratorCustomTitle(user: User, customTitle: String) =
    setChatAdministratorCustomTitle(user.id, customTitle)
