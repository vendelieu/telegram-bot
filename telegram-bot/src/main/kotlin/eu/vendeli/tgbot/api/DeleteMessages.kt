@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class DeleteMessagesAction(messageIds: List<Long>) : Action<Boolean>() {
    override val method = TgMethod("deleteMessage")
    override val returnType = getReturnType()

    init {
        parameters["message_ids"] = messageIds
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun deleteMessages(vararg messageId: Long) = DeleteMessagesAction(messageId.asList())
