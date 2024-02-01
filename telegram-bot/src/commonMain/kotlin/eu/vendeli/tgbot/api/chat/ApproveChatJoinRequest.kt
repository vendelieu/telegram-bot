@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class ApproveChatJoinRequestAction(userId: Long) : Action<Boolean>() {
    override val method = TgMethod("approveChatJoinRequest")
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId.toJsonElement()
    }
}

inline fun approveChatJoinRequest(userId: Long) = ApproveChatJoinRequestAction(userId)

inline fun approveChatJoinRequest(user: User) = approveChatJoinRequest(user.id)
