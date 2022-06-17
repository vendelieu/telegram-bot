package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.ChatMember
import eu.vendeli.tgbot.types.internal.TgMethod

class GetChatMemberAction(userId: Long) : Action<ChatMember> {
    override val method: TgMethod = TgMethod("getChatMember")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["user_id"] = userId
    }
}

fun getChatMember(userId: Long) = GetChatMemberAction(userId)
