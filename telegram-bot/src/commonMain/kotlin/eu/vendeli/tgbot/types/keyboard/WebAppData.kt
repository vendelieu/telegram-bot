package eu.vendeli.tgbot.types.keyboard

import kotlinx.serialization.Serializable

/**
 * Describes data sent from a Web App to the bot.
 *
 * [Api reference](https://core.telegram.org/bots/api#webappdata)
 * @property data The data. Be aware that a bad client can send arbitrary data in this field.
 * @property buttonText Text of the web_app keyboard button from which the Web App was opened. Be aware that a bad client can send arbitrary data in this field.
 */
@Serializable
data class WebAppData(
    val data: String? = null,
    val buttonText: String,
)
