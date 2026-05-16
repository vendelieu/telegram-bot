package eu.vendeli.tgbot.types.bot

import eu.vendeli.tgbot.interfaces.marker.MultipleResponse
import eu.vendeli.tgbot.types.User
import kotlinx.serialization.Serializable

/**
 * This object describes the access settings of a bot.
 *
 * [Api reference](https://core.telegram.org/bots/api#botaccesssettings)
 * @property isAccessRestricted True, if only selected users can access the bot. The bot's owner can always access it.
 * @property addedUsers Optional. The list of other users who have access to the bot if the access is restricted
 */
@Serializable
data class BotAccessSettings(
    val isAccessRestricted: Boolean,
    val addedUsers: List<User>? = null,
) : MultipleResponse
