package eu.vendeli.tgbot.types.bot

import kotlinx.serialization.Serializable

/**
 * This object represents the bot's description.
 *
 * [Api reference](https://core.telegram.org/bots/api#botdescription)
 * @property description The bot's description
 */
@Serializable
data class BotDescription(val description: String)
