package eu.vendeli.tgbot.types.bot

import eu.vendeli.tgbot.interfaces.MultipleResponse
import kotlinx.serialization.Serializable

/**
 * This object represents a bot command.
 *
 * [Api reference](https://core.telegram.org/bots/api#botcommand)
 * @property command Text of the command; 1-32 characters. Can contain only lowercase English letters, digits and underscores.
 * @property description Description of the command; 1-256 characters.
 */
@Serializable
data class BotCommand(
    val command: String,
    val description: String,
) : MultipleResponse
