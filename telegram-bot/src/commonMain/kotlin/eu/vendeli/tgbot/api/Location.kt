@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.interfaces.action.BusinessActionExt
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.internal.options.LocationOptions
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

@TgAPI
class SendLocationAction(
    latitude: Float,
    longitude: Float,
) : Action<Message>(),
    BusinessActionExt<Message>,
    OptionsFeature<SendLocationAction, LocationOptions>,
    MarkupFeature<SendLocationAction> {
    @TgAPI.Name("sendLocation")
    override val method = "sendLocation"
    override val returnType = getReturnType()
    override val options = LocationOptions()

    init {
        parameters["latitude"] = latitude.toJsonElement()
        parameters["longitude"] = longitude.toJsonElement()
    }
}

/**
 * Use this method to send point on the map. On success, the sent Message is returned.
 *
 * [Api reference](https://core.telegram.org/bots/api#sendlocation)
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
 * @param latitude Latitude of the location
 * @param longitude Longitude of the location
 * @param horizontalAccuracy The radius of uncertainty for the location, measured in meters; 0-1500
 * @param livePeriod Period in seconds during which the location will be updated (see Live Locations, should be between 60 and 86400, or 0x7FFFFFFF for live locations that can be edited indefinitely.
 * @param heading For live locations, a direction in which the user is moving, in degrees. Must be between 1 and 360 if specified.
 * @param proximityAlertRadius For live locations, a maximum distance for proximity alerts about approaching another chat member, in meters. Must be between 1 and 100000 if specified.
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param allowPaidBroadcast Pass True to allow up to 1000 messages per second, ignoring broadcasting limits for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance
 * @param messageEffectId Unique identifier of the message effect to be added to the message; for private chats only
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove a reply keyboard or to force a reply from the user
 * @returns [Message]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun sendLocation(latitude: Float, longitude: Float) = location(latitude, longitude)

@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun location(latitude: Float, longitude: Float) = SendLocationAction(latitude, longitude)
