package eu.vendeli.tgbot.types.inline

import eu.vendeli.tgbot.types.LocationContent
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.ChatType
import kotlinx.serialization.Serializable

/**
 * This object represents an incoming inline query. When the user sends an empty query, your bot could return some default or trending results.
 *
 * [Api reference](https://core.telegram.org/bots/api#inlinequery)
 * @property id Unique identifier for this query
 * @property from Sender
 * @property query Text of the query (up to 256 characters)
 * @property offset Offset of the results to be returned, can be controlled by the bot
 * @property chatType Optional. Type of the chat from which the inline query was sent. Can be either "sender" for a private chat with the inline query sender, "private", "group", "supergroup", or "channel". The chat type should be always known for requests sent from official clients and most third-party clients, unless the request was sent from a secret chat
 * @property location Optional. Sender location, only for bots that request user location
 */
@Serializable
data class InlineQuery(
    val id: String,
    val from: User,
    val query: String,
    val offset: String,
    val chatType: ChatType? = null,
    val location: LocationContent? = null,
)
