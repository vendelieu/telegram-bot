package eu.vendeli.tgbot.types.business

import eu.vendeli.tgbot.types.Location
import kotlinx.serialization.Serializable

@Serializable
data class BusinessLocation(
    val address: String,
    val location: Location? = null,
)
