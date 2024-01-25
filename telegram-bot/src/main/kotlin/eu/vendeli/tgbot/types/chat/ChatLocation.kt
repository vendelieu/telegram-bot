package eu.vendeli.tgbot.types.chat

import eu.vendeli.tgbot.types.LocationContent
import kotlinx.serialization.Serializable

@Serializable
data class ChatLocation(
    val location: LocationContent,
    val address: String,
)
