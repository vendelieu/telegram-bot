package eu.vendeli.tgbot.types.boost

import kotlinx.serialization.Serializable

/**
 * This object contains information about a chat boost.
 * @property boostId Unique identifier of the boost
 * @property addDate Point in time (Unix timestamp) when the chat was boosted
 * @property expirationDate Point in time (Unix timestamp) when the boost will automatically expire, unless the booster's Telegram Premium subscription is prolonged
 * @property source Source of the added boost
 * Api reference: https://core.telegram.org/bots/api#chatboost
*/
@Serializable
data class ChatBoost(
    val boostId: String,
    val addDate: Long,
    val expirationDate: Long,
    val source: ChatBoostSource,
)
