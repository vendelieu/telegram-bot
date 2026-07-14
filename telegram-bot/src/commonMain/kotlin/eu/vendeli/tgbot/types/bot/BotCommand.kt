package eu.vendeli.tgbot.types.bot

import eu.vendeli.tgbot.interfaces.marker.MultipleResponse
import kotlinx.serialization.Serializable

/**
 * This object represents a bot command.
 *
 * [Api reference](https://core.telegram.org/bots/api#botcommand)
 * @property command Text of the command; 1-32 characters. Can contain only lowercase English letters, digits and underscores.
 * @property description Description of the command; 1-256 characters
 * @property isEphemeral Optional. True, if the command sends an ephemeral message, which can be seen only by the sender of the message and the bot
 */
@Serializable
data class BotCommand(
    val command: String,
    val description: String,
    val isEphemeral: Boolean? = null,
) : MultipleResponse
