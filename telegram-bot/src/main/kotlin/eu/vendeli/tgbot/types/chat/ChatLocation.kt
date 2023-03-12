package eu.vendeli.tgbot.types.chat

import eu.vendeli.tgbot.types.LocationContent

data class ChatLocation(
    val location: LocationContent,
    val address: String,
)
