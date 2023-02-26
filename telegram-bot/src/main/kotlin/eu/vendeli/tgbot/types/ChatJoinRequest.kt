package eu.vendeli.tgbot.types

data class ChatJoinRequest(
    val chat: Chat,
    val from: User,
    val userChatId: Long,
    val date: Int,
    val bio: String? = null,
    val inviteLink: ChatInviteLink? = null,
)
