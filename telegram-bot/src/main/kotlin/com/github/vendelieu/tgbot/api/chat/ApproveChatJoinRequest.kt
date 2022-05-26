package com.github.vendelieu.tgbot.api.chat

import com.github.vendelieu.tgbot.interfaces.Action
import com.github.vendelieu.tgbot.types.internal.TgMethod

class ApproveChatJoinRequestAction(userId: String) : Action<Boolean> {
    override val method: TgMethod = TgMethod("approveChatJoinRequest")
    override val parameters: MutableMap<String, Any> = mutableMapOf()

    init {
        parameters["user_id"] = userId
    }
}

fun approveChatJoinRequest(userId: String) = ApproveChatJoinRequestAction(userId)
