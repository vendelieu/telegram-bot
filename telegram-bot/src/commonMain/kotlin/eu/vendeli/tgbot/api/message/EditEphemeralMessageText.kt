@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.message

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.interfaces.features.EntitiesFeature
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.options.EditEphemeralMessageOptions
import eu.vendeli.tgbot.utils.builders.EntitiesCtxBuilder
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class EditEphemeralMessageTextAction private constructor() :
    Action<Boolean>(),
    EntitiesCtxBuilder<EditEphemeralMessageTextAction>,
    OptionsFeature<EditEphemeralMessageTextAction, EditEphemeralMessageOptions>,
    MarkupFeature<EditEphemeralMessageTextAction>,
    EntitiesFeature<EditEphemeralMessageTextAction> {
        @TgAPI.Name("editEphemeralMessageText")
        override val method = "editEphemeralMessageText"
        override val returnType = getReturnType()
        override val options = EditEphemeralMessageOptions()

        constructor(receiverUserId: Long, ephemeralMessageId: Long, text: String) : this() {
            parameters["receiver_user_id"] = receiverUserId.toJsonElement()
            parameters["ephemeral_message_id"] = ephemeralMessageId.toJsonElement()
            parameters["text"] = text.toJsonElement()
        }

        internal constructor(
            receiverUserId: Long,
            ephemeralMessageId: Long,
            block: EntitiesCtxBuilder<EditEphemeralMessageTextAction>.() -> String,
        ) : this() {
            parameters["receiver_user_id"] = receiverUserId.toJsonElement()
            parameters["ephemeral_message_id"] = ephemeralMessageId.toJsonElement()
            parameters["text"] = block(this).toJsonElement()
        }
    }

/**
 * Use this method to edit an ephemeral text message. Note that it is not guaranteed that the user will receive the message edit event, especially if they are offline. On success, True is returned.
 *
 * [Api reference](https://core.telegram.org/bots/api#editephemeralmessagetext)
 * @param chatId Unique identifier for the target chat or username of the target supergroup in the format @username
 * @param receiverUserId Identifier of the user who received the message
 * @param ephemeralMessageId Identifier of the ephemeral message to edit
 * @param text New text of the message, 1-4096 characters after entity parsing
 * @param parseMode Mode for parsing entities in the message text. See formatting options for more details.
 * @param entities A JSON-serialized list of special entities that appear in message text, which can be specified instead of parse_mode
 * @param linkPreviewOptions Link preview generation options for the message
 * @param replyMarkup A JSON-serialized object for an inline keyboard
 * @returns [Boolean]
 */
@TgAPI
inline fun editEphemeralMessageText(receiverUserId: Long, ephemeralMessageId: Long, text: String) =
    EditEphemeralMessageTextAction(receiverUserId, ephemeralMessageId, text)

@TgAPI
fun editEphemeralMessageText(
    receiverUserId: Long,
    ephemeralMessageId: Long,
    block: EntitiesCtxBuilder<EditEphemeralMessageTextAction>.() -> String,
) = EditEphemeralMessageTextAction(receiverUserId, ephemeralMessageId, block)
