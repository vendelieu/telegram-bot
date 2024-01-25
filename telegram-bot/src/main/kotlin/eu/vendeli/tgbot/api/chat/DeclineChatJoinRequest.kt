@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class DeclineChatJoinRequestAction(userId: Long) : Action<Boolean>() {
    override val method = TgMethod("declineChatJoinRequest")
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId.toJsonElement()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun declineChatJoinRequest(userId: Long) = DeclineChatJoinRequestAction(userId)

@Suppress("NOTHING_TO_INLINE")
inline fun declineChatJoinRequest(user: User) = declineChatJoinRequest(user.id)
