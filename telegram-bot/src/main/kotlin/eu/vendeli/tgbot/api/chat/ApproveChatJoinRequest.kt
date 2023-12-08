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

@Suppress("NOTHING_TO_INLINE")
inline fun approveChatJoinRequest(userId: Long) = ApproveChatJoinRequestAction(userId)

@Suppress("NOTHING_TO_INLINE")
inline fun approveChatJoinRequest(user: User) = approveChatJoinRequest(user.id)
