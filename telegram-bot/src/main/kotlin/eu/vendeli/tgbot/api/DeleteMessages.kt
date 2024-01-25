@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class DeleteMessagesAction(messageIds: List<Long>) : Action<Boolean>() {
    override val method = TgMethod("deleteMessages")
    override val returnType = getReturnType()

    init {
        parameters["message_ids"] = messageIds.toJsonElement()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun deleteMessages(messageIds: List<Long>) = DeleteMessagesAction(messageIds)

@Suppress("NOTHING_TO_INLINE")
inline fun deleteMessages(vararg messageId: Long) = deleteMessages(messageId.asList())
