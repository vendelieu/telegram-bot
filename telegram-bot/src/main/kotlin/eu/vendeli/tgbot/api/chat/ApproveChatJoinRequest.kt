@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class ApproveChatJoinRequestAction(userId: Long) : Action<Boolean>() {
    override val method = TgMethod("approveChatJoinRequest")
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId
    }
}

fun approveChatJoinRequest(userId: Long) = ApproveChatJoinRequestAction(userId)
fun approveChatJoinRequest(user: User) = ApproveChatJoinRequestAction(user.id)
