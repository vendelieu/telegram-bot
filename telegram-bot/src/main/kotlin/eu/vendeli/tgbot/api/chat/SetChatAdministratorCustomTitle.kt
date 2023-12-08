@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class SetChatAdministratorCustomTitleAction(userId: Long, customTitle: String) : Action<Boolean>() {
    override val method = TgMethod("setChatAdministratorCustomTitle")
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId
        parameters["custom_title"] = customTitle
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun setChatAdministratorCustomTitle(userId: Long, customTitle: String) =
    SetChatAdministratorCustomTitleAction(userId, customTitle)

@Suppress("NOTHING_TO_INLINE")
inline fun setChatAdministratorCustomTitle(user: User, customTitle: String) =
    setChatAdministratorCustomTitle(user.id, customTitle)
