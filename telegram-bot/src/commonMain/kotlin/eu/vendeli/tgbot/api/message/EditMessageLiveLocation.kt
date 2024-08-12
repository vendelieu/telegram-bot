@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.message

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.interfaces.action.BusinessActionExt
import eu.vendeli.tgbot.interfaces.action.InlineActionExt
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.internal.options.EditMessageLiveLocationOptions
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

@TgAPI
class EditMessageLiveLocationAction :
    Action<Message>,
    InlineActionExt<Message>,
    BusinessActionExt<Message>,
    OptionsFeature<EditMessageLiveLocationAction, EditMessageLiveLocationOptions>,
    MarkupFeature<EditMessageLiveLocationAction> {
    @TgAPI.Name("editMessageLiveLocation")
    override val method = "editMessageLiveLocation"
    override val returnType = getReturnType()
    override val options = EditMessageLiveLocationOptions()

    constructor(messageId: Long, latitude: Float, longitude: Float) {
        parameters["message_id"] = messageId.toJsonElement()
        parameters["latitude"] = latitude.toJsonElement()
        parameters["longitude"] = longitude.toJsonElement()
    }

    constructor(latitude: Float, longitude: Float) {
        parameters["latitude"] = latitude.toJsonElement()
        parameters["longitude"] = longitude.toJsonElement()
    }
}

/**
 * Use this method to edit live location messages. A location can be edited until its live_period expires or editing is explicitly disabled by a call to stopMessageLiveLocation. On success, if the edited message is not an inline message, the edited Message is returned, otherwise True is returned.
 *
 * [Api reference](https://core.telegram.org/bots/api#editmessagelivelocation)
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message to be edited was sent
 * @param chatId Required if inline_message_id is not specified. Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageId Required if inline_message_id is not specified. Identifier of the message to edit
 * @param inlineMessageId Required if chat_id and message_id are not specified. Identifier of the inline message
 * @param latitude Latitude of new location
 * @param longitude Longitude of new location
 * @param livePeriod New period in seconds during which the location can be updated, starting from the message send date. If 0x7FFFFFFF is specified, then the location can be updated forever. Otherwise, the new value must not exceed the current live_period by more than a day, and the live location expiration date must remain within the next 90 days. If not specified, then live_period remains unchanged
 * @param horizontalAccuracy The radius of uncertainty for the location, measured in meters; 0-1500
 * @param heading Direction in which the user is moving, in degrees. Must be between 1 and 360 if specified.
 * @param proximityAlertRadius The maximum distance for proximity alerts about approaching another chat member, in meters. Must be between 1 and 100000 if specified.
 * @param replyMarkup A JSON-serialized object for a new inline keyboard.
 * @returns [Message]|[Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun editMessageLiveLocation(messageId: Long, latitude: Float, longitude: Float) =
    EditMessageLiveLocationAction(messageId, latitude, longitude)

@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun editMessageLiveLocation(
    latitude: Float,
    longitude: Float,
) = EditMessageLiveLocationAction(latitude, longitude)
