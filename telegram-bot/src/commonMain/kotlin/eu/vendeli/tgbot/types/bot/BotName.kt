package eu.vendeli.tgbot.types.bot

import kotlinx.serialization.Serializable

/**
 * This object represents the bot's name.
 * @property name The bot's name
 * Api reference: https://core.telegram.org/bots/api#botname
*/
@Serializable
data class BotName(val name: String)
