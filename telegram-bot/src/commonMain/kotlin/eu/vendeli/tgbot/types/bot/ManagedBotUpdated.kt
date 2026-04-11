package eu.vendeli.tgbot.types.bot

import eu.vendeli.tgbot.types.User
import kotlinx.serialization.Serializable

/**
 * This object contains information about the creation or token update of a bot that is managed by the current bot.
 *
 * [Api reference](https://core.telegram.org/bots/api#managedbotupdated)
 * @property user User that created the bot
 * @property bot Information about the bot. Token of the bot can be fetched using the method getManagedBotToken.
 */
@Serializable
data class ManagedBotUpdated(
    val user: User,
    val bot: User,
)
