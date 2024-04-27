package eu.vendeli.tgbot.types.boost

import kotlinx.serialization.Serializable

/**
 * This object represents a service message about a user boosting a chat.
 *
 * [Api reference](https://core.telegram.org/bots/api#chatboostadded)
 * @property boostCount Number of boosts added by the user
 */
@Serializable
data class ChatBoostAdded(
    val boostCount: Int,
)
