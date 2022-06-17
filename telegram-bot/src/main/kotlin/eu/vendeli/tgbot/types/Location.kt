package eu.vendeli.tgbot.types

data class Location(
    val longitude: Float,
    val latitude: Float,
    val horizontalAccuracy: Float? = null,
    val livePeriod: Int? = null,
    val heading: Int? = null,
    val proximityAlertRadius: Int?,
)
