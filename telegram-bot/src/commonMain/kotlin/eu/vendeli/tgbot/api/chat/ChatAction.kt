@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.chat.ChatAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.encodeWith
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class SendChatAction(action: ChatAction, messageThreadId: Int? = null) : Action<Boolean>() {
    override val method = TgMethod("sendChatAction")
    override val returnType = getReturnType()

    init {
        parameters["action"] = action.encodeWith(ChatAction.serializer())
        if (messageThreadId != null) parameters["message_thread_id"] = messageThreadId.toJsonElement()
    }
}

inline fun chatAction(action: ChatAction, messageThreadId: Int? = null) = SendChatAction(action, messageThreadId)

inline fun chatAction(messageThreadId: Int? = null, block: () -> ChatAction) = chatAction(block(), messageThreadId)

inline fun sendChatAction(messageThreadId: Int? = null, block: () -> ChatAction) = chatAction(block(), messageThreadId)

inline fun sendChatAction(action: ChatAction, messageThreadId: Int? = null) = chatAction(action, messageThreadId)
