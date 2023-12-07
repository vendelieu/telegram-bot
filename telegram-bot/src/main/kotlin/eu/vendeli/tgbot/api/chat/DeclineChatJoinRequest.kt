@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class DeclineChatJoinRequestAction(userId: Long) : Action<Boolean>() {
    override val method = TgMethod("declineChatJoinRequest")
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId
    }
}

fun declineChatJoinRequest(userId: Long) = DeclineChatJoinRequestAction(userId)
fun declineChatJoinRequest(user: User) = DeclineChatJoinRequestAction(user.id)
