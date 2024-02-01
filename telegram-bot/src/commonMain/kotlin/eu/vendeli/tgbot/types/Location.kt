package eu.vendeli.tgbot.types

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val longitude: Float,
    val latitude: Float,
    val horizontalAccuracy: Float? = null,
    val livePeriod: Int? = null,
    val heading: Int? = null,
    val proximityAlertRadius: Int? = null,
)
