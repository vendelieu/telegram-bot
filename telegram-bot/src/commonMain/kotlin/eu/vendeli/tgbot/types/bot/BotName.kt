package eu.vendeli.tgbot.types.bot

import kotlinx.serialization.Serializable

/**
 * This object represents the bot's name.
 *
 * [Api reference](https://core.telegram.org/bots/api#botname)
 * @property name The bot's name
 */
@Serializable
data class BotName(
    val name: String,
)
