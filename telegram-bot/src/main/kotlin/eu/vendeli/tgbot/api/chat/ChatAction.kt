@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.chat.ChatAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class SendChatAction(action: ChatAction, messageThreadId: Int? = null) : Action<Boolean>() {
    override val method = TgMethod("sendChatAction")
    override val returnType = getReturnType()

    init {
        parameters["action"] = action
        if (messageThreadId != null) parameters["message_thread_id"] = messageThreadId
    }
}

fun sendChatAction(messageThreadId: Int? = null, block: () -> ChatAction) = chatAction(block(), messageThreadId)
fun sendChatAction(action: ChatAction, messageThreadId: Int? = null) = chatAction(action, messageThreadId)
fun chatAction(messageThreadId: Int? = null, block: () -> ChatAction) = SendChatAction(block(), messageThreadId)
fun chatAction(action: ChatAction, messageThreadId: Int? = null) = SendChatAction(action, messageThreadId)
