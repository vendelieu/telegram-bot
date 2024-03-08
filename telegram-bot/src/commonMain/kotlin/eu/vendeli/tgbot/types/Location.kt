package eu.vendeli.tgbot.types

import kotlinx.serialization.Serializable

/**
 * This object represents a point on the map.
 * Api reference: https://core.telegram.org/bots/api#location
 * @property latitude Latitude as defined by sender
 * @property longitude Longitude as defined by sender
 * @property horizontalAccuracy Optional. The radius of uncertainty for the location, measured in meters; 0-1500
 * @property livePeriod Optional. Time relative to the message sending date, during which the location can be updated; in seconds. For active live locations only.
 * @property heading Optional. The direction in which user is moving, in degrees; 1-360. For active live locations only.
 * @property proximityAlertRadius Optional. The maximum distance for proximity alerts about approaching another chat member, in meters. For sent live locations only.
*/
@Serializable
data class Location(
    val longitude: Float,
    val latitude: Float,
    val horizontalAccuracy: Float? = null,
    val livePeriod: Int? = null,
    val heading: Int? = null,
    val proximityAlertRadius: Int? = null,
)
