package eu.vendeli.tgbot.types.business

import eu.vendeli.tgbot.types.User
import kotlinx.serialization.Serializable

@Serializable
data class BusinessConnection(
    val id: String,
    val user: User,
    val userChatId: Long,
    val date: Long,
    val canReply: Boolean,
    val isEnabled: Boolean,
)
