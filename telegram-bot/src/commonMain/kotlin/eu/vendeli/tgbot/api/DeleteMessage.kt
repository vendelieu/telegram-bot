@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class DeleteMessageAction(messageId: Long) : Action<Boolean>() {
    override val method = TgMethod("deleteMessage")
    override val returnType = getReturnType()

    init {
        parameters["message_id"] = messageId.toJsonElement()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun deleteMessage(messageId: Long) = DeleteMessageAction(messageId)
