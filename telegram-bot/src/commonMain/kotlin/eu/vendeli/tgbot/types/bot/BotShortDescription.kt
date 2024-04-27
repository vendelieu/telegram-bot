package eu.vendeli.tgbot.types.bot

import kotlinx.serialization.Serializable

/**
 * This object represents the bot's short description.
 *
 * [Api reference](https://core.telegram.org/bots/api#botshortdescription)
 * @property shortDescription The bot's short description
 */
@Serializable
data class BotShortDescription(val shortDescription: String)
