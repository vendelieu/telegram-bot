package eu.vendeli.tgbot.types.options

import kotlinx.serialization.Serializable

@Serializable
data class EditMessageLiveLocationOptions(
    var horizontalAccuracy: Float? = null,
    var heading: Int? = null,
    var proximityAlertRadius: Int? = null,
    var livePeriod: Int? = null,
) : Options
