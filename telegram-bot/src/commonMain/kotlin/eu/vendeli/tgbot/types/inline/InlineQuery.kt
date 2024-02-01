package eu.vendeli.tgbot.types.inline

import eu.vendeli.tgbot.types.LocationContent
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.ChatType
import kotlinx.serialization.Serializable

@Serializable
data class InlineQuery(
    val id: String,
    val from: User,
    val query: String,
    val offset: String,
    val chatType: ChatType? = null,
    val location: LocationContent? = null,
)
