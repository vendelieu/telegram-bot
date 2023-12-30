package eu.vendeli.tgbot.types

data class Venue(
    val location: LocationContent,
    val title: String,
    val address: String,
    val foursquareId: String? = null,
    val fourSquareType: String? = null,
    val googlePlaceId: String? = null,
    val googlePlaceType: String? = null,
)
