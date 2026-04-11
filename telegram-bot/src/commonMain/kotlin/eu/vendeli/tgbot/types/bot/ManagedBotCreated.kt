package eu.vendeli.tgbot.types.bot

import eu.vendeli.tgbot.types.User
import kotlinx.serialization.Serializable

/**
 * This object contains information about the bot that was created to be managed by the current bot.
 *
 * [Api reference](https://core.telegram.org/bots/api#managedbotcreated)
 * @property bot Information about the bot. The bot's token can be fetched using the method getManagedBotToken.
 */
@Serializable
data class ManagedBotCreated(
    val bot: User,
)
