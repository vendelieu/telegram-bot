package eu.vendeli.tgbot.types.chat

import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class ChatJoinRequest(
    val chat: Chat,
    val from: User,
    val userChatId: Long,
    @Serializable(InstantSerializer::class)
    val date: Instant,
    val bio: String? = null,
    val inviteLink: ChatInviteLink? = null,
)
