package eu.vendeli.tgbot.types.business

import eu.vendeli.tgbot.types.chat.Chat
import kotlinx.serialization.Serializable

@Serializable
data class BusinessMessagesDeleted(
    val businessConnectionId: String,
    val chat: Chat,
    val messageIds: List<Int>,
)
