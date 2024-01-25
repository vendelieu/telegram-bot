package eu.vendeli.tgbot.types.chat

import kotlinx.serialization.Serializable

@Serializable
data class ChatShared(
    val requestId: Int,
    val chatId: Long,
)
