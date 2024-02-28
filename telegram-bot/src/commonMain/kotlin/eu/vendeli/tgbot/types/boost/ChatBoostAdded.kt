package eu.vendeli.tgbot.types.boost

import kotlinx.serialization.Serializable

/**
 * This object represents a service message about a user boosting a chat.
 * @property boostCount Number of boosts added by the user
 * Api reference: https://core.telegram.org/bots/api#chatboostadded
*/
@Serializable
data class ChatBoostAdded(
    val boostCount: Int,
)
