package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod

class GetChatMemberCountAction : Action<Int> {
    override val method: TgMethod = TgMethod("getChatMemberCount")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()
}

fun getChatMemberCount() = GetChatMemberCountAction()
