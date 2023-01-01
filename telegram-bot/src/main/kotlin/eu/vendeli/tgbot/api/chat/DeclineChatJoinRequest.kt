@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod

class DeclineChatJoinRequestAction(userId: String) : Action<Boolean> {
    override val method: TgMethod = TgMethod("declineChatJoinRequest")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["user_id"] = userId
    }
}

fun declineChatJoinRequest(userId: String) = DeclineChatJoinRequestAction(userId)
