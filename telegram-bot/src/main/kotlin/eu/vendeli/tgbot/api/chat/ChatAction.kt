@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.chat.ChatAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class SendChatAction(action: ChatAction, messageThreadId: Int? = null) : Action<Boolean>, ActionState() {
    override val TgAction<Boolean>.method: TgMethod
        get() = TgMethod("sendChatAction")
    override val TgAction<Boolean>.returnType: Class<Boolean>
        get() = getReturnType()

    init {
        parameters["action"] = action
        if (messageThreadId != null) parameters["message_thread_id"] = messageThreadId
    }
}

fun chatAction(messageThreadId: Int? = null, block: () -> ChatAction) = SendChatAction(block(), messageThreadId)
fun chatAction(action: ChatAction, messageThreadId: Int? = null) = SendChatAction(action, messageThreadId)
