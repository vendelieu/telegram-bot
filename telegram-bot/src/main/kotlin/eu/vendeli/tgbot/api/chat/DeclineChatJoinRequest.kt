@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class DeclineChatJoinRequestAction(userId: String) : Action<Boolean>, ActionState() {
    override val method: TgMethod = TgMethod("declineChatJoinRequest")
    override val returnType = getReturnType()

    init {
        parameters["user_id"] = userId
    }
}

fun declineChatJoinRequest(userId: String) = DeclineChatJoinRequestAction(userId)
