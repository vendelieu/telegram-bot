@file:Suppress("MatchingDeclarationName")
package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.ChatAction
import eu.vendeli.tgbot.types.internal.TgMethod

class SendChatAction(action: ChatAction) : Action<Boolean> {
    override val method: TgMethod = TgMethod("sendChatAction")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["action"] = action
    }
}

fun chatAction(block: () -> ChatAction) = SendChatAction(block())
fun chatAction(action: ChatAction) = SendChatAction(action)
