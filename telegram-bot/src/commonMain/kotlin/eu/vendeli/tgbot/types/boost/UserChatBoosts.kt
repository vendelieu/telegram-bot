package eu.vendeli.tgbot.types.boost

import kotlinx.serialization.Serializable

/**
 * This object represents a list of boosts added to a chat by a user.
 * Api reference: https://core.telegram.org/bots/api#userchatboosts
 * @property boosts The list of boosts added to the chat by the user
*/
@Serializable
data class UserChatBoosts(
    val boosts: List<ChatBoost>,
)
