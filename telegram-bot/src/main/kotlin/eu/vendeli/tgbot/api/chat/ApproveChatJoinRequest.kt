package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod

class ApproveChatJoinRequestAction(userId: String) : Action<Boolean> {
    override val method: TgMethod = TgMethod("approveChatJoinRequest")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["user_id"] = userId
    }
}

fun approveChatJoinRequest(userId: String) = ApproveChatJoinRequestAction(userId)
