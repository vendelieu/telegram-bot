package eu.vendeli.tgbot.types.inline

import eu.vendeli.tgbot.types.keyboard.WebAppInfo
import kotlinx.serialization.Serializable

@Serializable
data class InlineQueryResultsButton(
    val text: String,
    val webApp: WebAppInfo? = null,
    val startParameter: String? = null,
)
