package eu.vendeli.tgbot.types.inline

import eu.vendeli.tgbot.types.keyboard.WebAppInfo

data class InlineQueryResultsButton(
    val text: String,
    val webApp: WebAppInfo? = null,
    val startParameter: String? = null,
)
