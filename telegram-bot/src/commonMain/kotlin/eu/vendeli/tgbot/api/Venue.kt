
@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.internal.options.VenueOptions
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

@TgAPI
class SendVenueAction(
    latitude: Float,
    longitude: Float,
    title: String,
    address: String,
) : Action<Message>(),
    OptionsFeature<SendVenueAction, VenueOptions>,
    MarkupFeature<SendVenueAction> {
    @TgAPI.Name("sendVenue")
    override val method = "sendVenue"
    override val returnType = getReturnType()
    override val options = VenueOptions()
    init {
        parameters["latitude"] = latitude.toJsonElement()
        parameters["longitude"] = longitude.toJsonElement()
        parameters["title"] = title.toJsonElement()
        parameters["address"] = address.toJsonElement()
    }
}

/**
 * Use this method to send information about a venue. On success, the sent Message is returned.
 *
 * [Api reference](https://core.telegram.org/bots/api#sendvenue)
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
 * @param latitude Latitude of the venue
 * @param longitude Longitude of the venue
 * @param title Name of the venue
 * @param address Address of the venue
 * @param foursquareId Foursquare identifier of the venue
 * @param foursquareType Foursquare type of the venue, if known. (For example, "arts_entertainment/default", "arts_entertainment/aquarium" or "food/icecream".)
 * @param googlePlaceId Google Places identifier of the venue
 * @param googlePlaceType Google Places type of the venue. (See supported types.)
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param messageEffectId Unique identifier of the message effect to be added to the message; for private chats only
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove a reply keyboard or to force a reply from the user
 * @returns [Message]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun venue(latitude: Float, longitude: Float, title: String, address: String) =
    SendVenueAction(latitude, longitude, title, address)

@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun sendVenue(latitude: Float, longitude: Float, title: String, address: String) =
    venue(latitude, longitude, title, address)
