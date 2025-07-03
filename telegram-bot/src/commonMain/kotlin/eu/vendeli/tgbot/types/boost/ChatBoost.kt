package eu.vendeli.tgbot.types.boost

import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlin.time.Instant
import kotlinx.serialization.Serializable

/**
 * This object contains information about a chat boost.
 *
 * [Api reference](https://core.telegram.org/bots/api#chatboost)
 * @property boostId Unique identifier of the boost
 * @property addDate Point in time (Unix timestamp) when the chat was boosted
 * @property expirationDate Point in time (Unix timestamp) when the boost will automatically expire, unless the booster's Telegram Premium subscription is prolonged
 * @property source Source of the added boost
 */
@Serializable
data class ChatBoost(
    val boostId: String,
    @Serializable(InstantSerializer::class)
    val addDate: Instant,
    @Serializable(InstantSerializer::class)
    val expirationDate: Instant,
    val source: ChatBoostSource,
)
