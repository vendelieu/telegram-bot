@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.business

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class ReadBusinessMessageAction(
    businessConnectionId: String,
    messageId: Long,
) : Action<Boolean>() {
    @TgAPI.Name("readBusinessMessage")
    override val method = "readBusinessMessage"
    override val returnType = getReturnType()

    init {
        parameters["business_connection_id"] = businessConnectionId.toJsonElement()
        parameters["message_id"] = messageId.toJsonElement()
    }
}

/**
 * Marks incoming message as read on behalf of a business account. Requires the can_read_messages business bot right. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#readbusinessmessage)
 * @param businessConnectionId Unique identifier of the business connection on behalf of which to read the message
 * @param chatId Unique identifier of the chat in which the message was received. The chat must have been active in the last 24 hours.
 * @param messageId Unique identifier of the message to mark as read
 * @returns [Boolean]
 */
@TgAPI
inline fun readBusinessMessage(
    businessConnectionId: String,
    messageId: Long,
) = ReadBusinessMessageAction(businessConnectionId, messageId)
