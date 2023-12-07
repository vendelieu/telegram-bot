@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class DeleteMessageAction(messageId: Long) : Action<Boolean>() {
    override val method = TgMethod("deleteMessage")
    override val returnType = getReturnType()

    init {
        parameters["message_id"] = messageId
    }
}

fun deleteMessage(messageId: Long) = DeleteMessageAction(messageId)
