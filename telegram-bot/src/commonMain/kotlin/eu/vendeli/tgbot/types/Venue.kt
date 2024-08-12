package eu.vendeli.tgbot.types

import kotlinx.serialization.Serializable

/**
 * This object represents a venue.
 *
 * [Api reference](https://core.telegram.org/bots/api#venue)
 * @property location Venue location. Can't be a live location
 * @property title Name of the venue
 * @property address Address of the venue
 * @property foursquareId Optional. Foursquare identifier of the venue
 * @property foursquareType Optional. Foursquare type of the venue. (For example, "arts_entertainment/default", "arts_entertainment/aquarium" or "food/icecream".)
 * @property googlePlaceId Optional. Google Places identifier of the venue
 * @property googlePlaceType Optional. Google Places type of the venue. (See supported types.)
 */
@Serializable
data class Venue(
    val location: Location,
    val title: String,
    val address: String,
    val foursquareId: String? = null,
    val foursquareType: String? = null,
    val googlePlaceId: String? = null,
    val googlePlaceType: String? = null,
)
