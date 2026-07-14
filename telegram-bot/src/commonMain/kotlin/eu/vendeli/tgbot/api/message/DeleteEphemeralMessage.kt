@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.message

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class DeleteEphemeralMessageAction(
    receiverUserId: Long,
    ephemeralMessageId: Long,
) : Action<Boolean>() {
    @TgAPI.Name("deleteEphemeralMessage")
    override val method = "deleteEphemeralMessage"
    override val returnType = getReturnType()

    init {
        parameters["receiver_user_id"] = receiverUserId.toJsonElement()
        parameters["ephemeral_message_id"] = ephemeralMessageId.toJsonElement()
    }
}

/**
 * Use this method to delete an ephemeral message. Note that it is not guaranteed that the user will receive the message deletion event, especially if they are offline. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#deleteephemeralmessage)
 * @param chatId Unique identifier for the target chat or username of the target supergroup in the format @username
 * @param receiverUserId Identifier of the user who received the message
 * @param ephemeralMessageId Identifier of the ephemeral message to delete
 * @returns [Boolean]
 */
@TgAPI
inline fun deleteEphemeralMessage(receiverUserId: Long, ephemeralMessageId: Long) =
    DeleteEphemeralMessageAction(receiverUserId, ephemeralMessageId)
