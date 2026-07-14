@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.message

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class EditEphemeralMessageReplyMarkupAction(
    receiverUserId: Long,
    ephemeralMessageId: Long,
) : Action<Boolean>(),
    MarkupFeature<EditEphemeralMessageReplyMarkupAction> {
    @TgAPI.Name("editEphemeralMessageReplyMarkup")
    override val method = "editEphemeralMessageReplyMarkup"
    override val returnType = getReturnType()

    init {
        parameters["receiver_user_id"] = receiverUserId.toJsonElement()
        parameters["ephemeral_message_id"] = ephemeralMessageId.toJsonElement()
    }
}

/**
 * Use this method to edit only the reply markup of an ephemeral message. Note that it is not guaranteed that the user will receive the message edit event, especially if they are offline. On success, True is returned.
 *
 * [Api reference](https://core.telegram.org/bots/api#editephemeralmessagereplymarkup)
 * @param chatId Unique identifier for the target chat or username of the target supergroup in the format @username
 * @param receiverUserId Identifier of the user who received the message
 * @param ephemeralMessageId Identifier of the ephemeral message to edit
 * @param replyMarkup A JSON-serialized object for an inline keyboard
 * @returns [Boolean]
 */
@TgAPI
inline fun editEphemeralMessageReplyMarkup(receiverUserId: Long, ephemeralMessageId: Long) =
    EditEphemeralMessageReplyMarkupAction(receiverUserId, ephemeralMessageId)
