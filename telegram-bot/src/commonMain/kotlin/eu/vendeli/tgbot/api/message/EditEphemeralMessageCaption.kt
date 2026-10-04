@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.message

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.interfaces.features.CaptionFeature
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.options.EditEphemeralMessageCaptionOptions
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class EditEphemeralMessageCaptionAction(
    receiverUserId: Long,
    ephemeralMessageId: Long,
) : Action<Boolean>(),
    OptionsFeature<EditEphemeralMessageCaptionAction, EditEphemeralMessageCaptionOptions>,
    MarkupFeature<EditEphemeralMessageCaptionAction>,
    CaptionFeature<EditEphemeralMessageCaptionAction> {
    @TgAPI.Name("editEphemeralMessageCaption")
    override val method = "editEphemeralMessageCaption"
    override val returnType = getReturnType()
    override val options = EditEphemeralMessageCaptionOptions()
    override val entitiesFieldName: String = "caption_entities"

    init {
        parameters["receiver_user_id"] = receiverUserId.toJsonElement()
        parameters["ephemeral_message_id"] = ephemeralMessageId.toJsonElement()
    }
}

/**
 * Use this method to edit the caption of an ephemeral message. Note that it is not guaranteed that the user will receive the message edit event, especially if they are offline. On success, True is returned.
 *
 * [Api reference](https://core.telegram.org/bots/api#editephemeralmessagecaption)
 * @param chatId Unique identifier for the target chat or username of the target supergroup in the format @username
 * @param receiverUserId Identifier of the user who received the message
 * @param ephemeralMessageId Identifier of the ephemeral message to edit
 * @param caption New caption of the message, 0-1024 characters after entities parsing
 * @param parseMode Mode for parsing entities in the message caption. See formatting options for more details.
 * @param captionEntities A JSON-serialized list of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param showCaptionAboveMedia Pass True if the caption must be shown above the message media. Supported only for animation, photo and video messages.
 * @param replyMarkup A JSON-serialized object for an inline keyboard
 * @returns [Boolean]
 */
@TgAPI
inline fun editEphemeralMessageCaption(receiverUserId: Long, ephemeralMessageId: Long) =
    EditEphemeralMessageCaptionAction(receiverUserId, ephemeralMessageId)
