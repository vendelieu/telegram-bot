package eu.vendeli.tgbot.types.chat

import eu.vendeli.tgbot.types.User
import java.time.Instant

data class ChatJoinRequest(
    val chat: Chat,
    val from: User,
    val userChatId: Long,
    val date: Instant,
    val bio: String? = null,
    val inviteLink: ChatInviteLink? = null,
)
