package eu.vendeli.tgbot.types.chat

import eu.vendeli.tgbot.types.User

data class ChatMemberUpdated(
    val chat: Chat,
    val from: User,
    val date: Int,
    val oldChatMember: ChatMember,
    val newChatMember: ChatMember,
    val inviteLink: ChatInviteLink? = null,
)
