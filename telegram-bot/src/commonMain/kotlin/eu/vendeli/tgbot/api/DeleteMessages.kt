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

/**
 * Use this method to delete multiple messages simultaneously. If some of the specified messages can't be found, they are skipped. Returns True on success.
 * @param chatId Required 
 * @param messageIds Required 
 * @returns [Boolean]
 * Api reference: https://core.telegram.org/bots/api#deletemessages
*/
@Suppress("NOTHING_TO_INLINE")
inline fun deleteMessages(messageIds: List<Long>) = DeleteMessagesAction(messageIds)

@Suppress("NOTHING_TO_INLINE")
inline fun deleteMessages(vararg messageId: Long) = deleteMessages(messageId.asList())
