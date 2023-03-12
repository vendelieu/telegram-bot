package eu.vendeli.tgbot.types.chat

import eu.vendeli.tgbot.types.User

data class ChatJoinRequest(
    val chat: Chat,
    val from: User,
    val userChatId: Long,
    val date: Int,
    val bio: String? = null,
    val inviteLink: ChatInviteLink? = null,
)
