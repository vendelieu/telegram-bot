package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod

class LeaveChatAction : Action<Boolean> {
    override val method: TgMethod = TgMethod("leaveChat")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()
}

fun leaveChat() = LeaveChatAction()
