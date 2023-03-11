@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class SetChatAdministratorCustomTitleAction(userId: Long, customTitle: String) : Action<Boolean>, ActionState() {
    override val method: TgMethod = TgMethod("setChatAdministratorCustomTitle")
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId
        parameters["custom_title"] = customTitle
    }
}

fun setChatAdministratorCustomTitle(userId: Long, customTitle: String) =
    SetChatAdministratorCustomTitleAction(userId, customTitle)
