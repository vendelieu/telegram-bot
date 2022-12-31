@file:Suppress("MatchingDeclarationName")
package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod

class UnbanChatSenderChatAction(senderChatId: Long) : Action<Boolean> {
    override val method: TgMethod = TgMethod("unbanChatSenderChat")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["sender_chat_id"] = senderChatId
    }
}

fun unbanChatSenderChat(senderChatId: Long) = UnbanChatSenderChatAction(senderChatId)
