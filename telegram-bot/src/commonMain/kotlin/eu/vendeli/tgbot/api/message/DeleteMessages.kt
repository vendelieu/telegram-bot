@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.message

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.utils.encodeWith
import eu.vendeli.tgbot.utils.getReturnType
import kotlinx.serialization.builtins.serializer

@TgAPI
class DeleteMessagesAction(
    messageIds: List<Long>,
) : Action<Boolean>() {
    @TgAPI.Method("deleteMessages")
    override val method = "deleteMessages"
    override val returnType = getReturnType()

    init {
        parameters["message_ids"] = messageIds.encodeWith(Long.serializer())
    }
}

/**
 * Use this method to delete multiple messages simultaneously. If some of the specified messages can't be found, they are skipped. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#deletemessages)
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageIds A JSON-serialized list of 1-100 identifiers of messages to delete. See deleteMessage for limitations on which messages can be deleted
 * @returns [Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun deleteMessages(messageIds: List<Long>) = DeleteMessagesAction(messageIds)

@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun deleteMessages(vararg messageId: Long) = deleteMessages(messageId.asList())
