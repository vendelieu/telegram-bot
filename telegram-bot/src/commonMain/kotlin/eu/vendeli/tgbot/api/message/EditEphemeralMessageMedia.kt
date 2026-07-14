@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.message

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.types.media.InputMedia
import eu.vendeli.tgbot.utils.internal.encodeWith
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class EditEphemeralMessageMediaAction(
    receiverUserId: Long,
    ephemeralMessageId: Long,
    media: InputMedia,
) : Action<Boolean>(),
    MarkupFeature<EditEphemeralMessageMediaAction> {
    @TgAPI.Name("editEphemeralMessageMedia")
    override val method = "editEphemeralMessageMedia"
    override val returnType = getReturnType()

    init {
        parameters["receiver_user_id"] = receiverUserId.toJsonElement()
        parameters["ephemeral_message_id"] = ephemeralMessageId.toJsonElement()
        parameters["media"] = media.encodeWith(InputMedia.serializer())
    }
}

/**
 * Use this method to edit the media of an ephemeral message. Note that it is not guaranteed that the user will receive the message edit event, especially if they are offline. On success, True is returned.
 *
 * [Api reference](https://core.telegram.org/bots/api#editephemeralmessagemedia)
 * @param chatId Unique identifier for the target chat or username of the target supergroup in the format @username
 * @param receiverUserId Identifier of the user who received the message
 * @param ephemeralMessageId Identifier of the ephemeral message to edit
 * @param media A JSON-serialized object for the new media content of the message. A new file can't be uploaded; use a previously uploaded file via its file_id or specify a URL.
 * @param replyMarkup A JSON-serialized object for an inline keyboard
 * @returns [Boolean]
 */
@TgAPI
inline fun editEphemeralMessageMedia(receiverUserId: Long, ephemeralMessageId: Long, inputMedia: InputMedia) =
    EditEphemeralMessageMediaAction(receiverUserId, ephemeralMessageId, inputMedia)
