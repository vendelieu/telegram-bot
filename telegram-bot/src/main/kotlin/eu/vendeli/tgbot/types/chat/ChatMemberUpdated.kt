package eu.vendeli.tgbot.types.chat

import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class ChatMemberUpdated(
    val chat: Chat,
    val from: User,
    @Serializable(InstantSerializer::class)
    val date: Instant,
    val oldChatMember: ChatMember,
    val newChatMember: ChatMember,
    val inviteLink: ChatInviteLink? = null,
    val viaChatFolderInviteLink: Boolean? = null,
)
