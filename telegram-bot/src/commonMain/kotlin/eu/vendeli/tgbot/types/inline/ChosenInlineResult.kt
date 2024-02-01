package eu.vendeli.tgbot.types.inline

import eu.vendeli.tgbot.types.LocationContent
import eu.vendeli.tgbot.types.User
import kotlinx.serialization.Serializable

@Serializable
data class ChosenInlineResult(
    val resultId: String,
    val from: User,
    val location: LocationContent? = null,
    val inlineMessageId: String? = null,
    val query: String,
)
