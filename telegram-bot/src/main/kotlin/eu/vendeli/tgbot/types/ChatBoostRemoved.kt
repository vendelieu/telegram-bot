package eu.vendeli.tgbot.types

import eu.vendeli.tgbot.types.chat.Chat
import java.time.Instant

data class ChatBoostRemoved(
    val chat: Chat,
    val boostId: String,
    val removeDate: Instant,
    val source: ChatBoostSource,
)
