package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod

class UnpinChatMessageAction(messageId: Long) : Action<Boolean> {
    override val method: TgMethod = TgMethod("unpinChatMessage")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["message_id"] = messageId
    }
}

fun unpinChatMessage(messageId: Long) = UnpinChatMessageAction(messageId)
