package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod

class UnpinAllChatMessageAction : Action<Boolean> {
    override val method: TgMethod = TgMethod("unpinAllChatMessages")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()
}

fun unpinAllChatMessage() = UnpinAllChatMessageAction()
