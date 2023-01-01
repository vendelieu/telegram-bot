@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.ChatAction
import eu.vendeli.tgbot.types.internal.TgMethod

class SendChatAction(messageThreadId: Int? = null, action: ChatAction) : Action<Boolean> {
    override val method: TgMethod = TgMethod("sendChatAction")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["action"] = action
        if (messageThreadId != null) parameters["message_thread_id"] = messageThreadId
    }
}

fun chatAction(messageThreadId: Int? = null, block: () -> ChatAction) = SendChatAction(messageThreadId, block())
fun chatAction(messageThreadId: Int? = null, action: ChatAction) = SendChatAction(messageThreadId, action)
