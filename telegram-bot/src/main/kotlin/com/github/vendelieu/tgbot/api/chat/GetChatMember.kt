package com.github.vendelieu.tgbot.api.chat

import com.github.vendelieu.tgbot.interfaces.Action
import com.github.vendelieu.tgbot.types.ChatMember
import com.github.vendelieu.tgbot.types.internal.TgMethod

class GetChatMemberAction(userId: Long) : Action<ChatMember> {
    override val method: TgMethod = TgMethod("getChatMember")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["user_id"] = userId
    }
}

fun getChatMember(userId: Long) = GetChatMemberAction(userId)
