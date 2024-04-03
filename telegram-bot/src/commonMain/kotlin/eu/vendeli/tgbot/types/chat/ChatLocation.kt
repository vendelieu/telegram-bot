package eu.vendeli.tgbot.types.chat

import eu.vendeli.tgbot.types.LocationContent
import kotlinx.serialization.Serializable

/**
 * Represents a location to which a chat is connected.
 *
 * Api reference: https://core.telegram.org/bots/api#chatlocation
 * @property location The location to which the supergroup is connected. Can't be a live location.
 * @property address Location address; 1-64 characters, as defined by the chat owner
 */
@Serializable
data class ChatLocation(
    val location: LocationContent,
    val address: String,
)
