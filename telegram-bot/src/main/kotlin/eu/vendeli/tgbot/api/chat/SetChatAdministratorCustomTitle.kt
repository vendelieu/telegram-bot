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

fun setChatAdministratorCustomTitle(userId: Long, customTitle: String) =
    SetChatAdministratorCustomTitleAction(userId, customTitle)
fun setChatAdministratorCustomTitle(user: User, customTitle: String) =
    SetChatAdministratorCustomTitleAction(user.id, customTitle)
