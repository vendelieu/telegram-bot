package eu.vendeli.tgbot.types.bot

import kotlinx.serialization.Serializable

/**
 * This object represents the bot's short description.
 * @property shortDescription The bot's short description
 * Api reference: https://core.telegram.org/bots/api#botshortdescription
*/
@Serializable
data class BotShortDescription(val shortDescription: String)
