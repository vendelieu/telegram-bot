package eu.vendeli.tgbot.types.boost

import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

/**
 * This object represents a boost removed from a chat.
 *
 * Api reference: https://core.telegram.org/bots/api#chatboostremoved
 * @property chat Chat which was boosted
 * @property boostId Unique identifier of the boost
 * @property removeDate Point in time (Unix timestamp) when the boost was removed
 * @property source Source of the removed boost
 */
@Serializable
data class ChatBoostRemoved(
    val chat: Chat,
    val boostId: String,
    @Serializable(InstantSerializer::class)
    val removeDate: Instant,
    val source: ChatBoostSource,
)
