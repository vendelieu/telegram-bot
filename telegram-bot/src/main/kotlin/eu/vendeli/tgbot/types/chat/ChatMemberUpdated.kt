package eu.vendeli.tgbot.types.chat

import eu.vendeli.tgbot.types.User
import java.time.Instant

data class ChatMemberUpdated(
    val chat: Chat,
    val from: User,
    val date: Instant,
    val oldChatMember: ChatMember,
    val newChatMember: ChatMember,
    val inviteLink: ChatInviteLink? = null,
    val viaChatFolderInviteLink: Boolean? = null,
)
