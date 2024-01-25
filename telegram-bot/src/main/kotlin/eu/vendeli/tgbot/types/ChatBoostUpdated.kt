package eu.vendeli.tgbot.types

import eu.vendeli.tgbot.types.chat.Chat
import kotlinx.serialization.Serializable

@Serializable
data class ChatBoostUpdated(
    val chat: Chat,
    val boost: ChatBoost,
)
