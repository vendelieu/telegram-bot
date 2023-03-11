@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class BanChatSenderChatAction(senderChatId: Long) : Action<Boolean>, ActionState() {
    override val method: TgMethod = TgMethod("banChatSenderChat")
    override val returnType = getReturnType()

    init {
        parameters["sender_chat_id"] = senderChatId
    }
}

fun banChatSenderChat(senderChatId: Long) = BanChatSenderChatAction(senderChatId)
