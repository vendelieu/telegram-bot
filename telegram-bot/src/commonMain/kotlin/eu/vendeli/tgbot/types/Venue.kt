package eu.vendeli.tgbot.types

import kotlinx.serialization.Serializable

@Serializable
data class Venue(
    val location: LocationContent,
    val title: String,
    val address: String,
    val foursquareId: String? = null,
    val fourSquareType: String? = null,
    val googlePlaceId: String? = null,
    val googlePlaceType: String? = null,
)
