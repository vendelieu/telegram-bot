@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.business

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.utils.internal.encodeWith
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer

@TgAPI
class DeleteBusinessMessagesAction(
    businessConnectionId: String,
    messageIds: List<Long>,
) : SimpleAction<Boolean>() {
    @TgAPI.Name("deleteBusinessMessages")
    override val method = "deleteBusinessMessages"
    override val returnType = getReturnType()

    init {
        parameters["business_connection_id"] = businessConnectionId.toJsonElement()
        parameters["message_ids"] = messageIds.encodeWith(ListSerializer(Long.serializer()))
    }
}

/**
 * Delete messages on behalf of a business account. Requires the can_delete_sent_messages business bot right to delete messages sent by the bot itself, or the can_delete_all_messages business bot right to delete any message. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#deletebusinessmessages)
 * @param businessConnectionId Unique identifier of the business connection on behalf of which to delete the messages
 * @param messageIds A JSON-serialized list of 1-100 identifiers of messages to delete. All messages must be from the same chat. See deleteMessage for limitations on which messages can be deleted
 * @returns [Boolean]
 */
@TgAPI
@Suppress("NOTHING_TO_INLINE")
inline fun deleteBusinessMessages(
    businessConnectionId: String,
    messageIds: List<Long>,
) = DeleteBusinessMessagesAction(businessConnectionId, messageIds)
