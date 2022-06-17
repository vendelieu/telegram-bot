package eu.vendeli.tgbot.types

data class ChatMemberUpdated(
    val chat: Chat,
    val from: User,
    val date: Int,
    val oldChatMember: ChatMember,
    val newChatMember: ChatMember,
    val inviteLink: ChatInviteLink? = null,
)
