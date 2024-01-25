package eu.vendeli.tgbot.types

import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class ChatBoostRemoved(
    val chat: Chat,
    val boostId: String,
    @Serializable(InstantSerializer::class)
    val removeDate: Instant,
    val source: ChatBoostSource,
)
