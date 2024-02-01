@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.encodeWith
import eu.vendeli.tgbot.utils.getReturnType
import kotlinx.serialization.builtins.serializer

class DeleteMessagesAction(messageIds: List<Long>) : Action<Boolean>() {
    override val method = TgMethod("deleteMessages")
    override val returnType = getReturnType()

    init {
        parameters["message_ids"] = messageIds.encodeWith(Long.serializer())
    }
}

inline fun deleteMessages(messageIds: List<Long>) = DeleteMessagesAction(messageIds)

inline fun deleteMessages(vararg messageId: Long) = deleteMessages(messageId.asList())
