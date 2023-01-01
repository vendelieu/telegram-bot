@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod

class DeleteMessageAction(messageId: Long) : Action<Boolean> {
    override val method: TgMethod = TgMethod("deleteMessage")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["message_id"] = messageId
    }
}

fun deleteMessage(messageId: Long) = DeleteMessageAction(messageId)
