package eu.vendeli.tgbot.types.chat

import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class ChatInviteLink(
    val inviteLink: String,
    val creator: User,
    val createsJoinRequest: Boolean,
    val isPrimary: Boolean,
    val isRevoked: Boolean,
    val name: String? = null,
    @Serializable(InstantSerializer::class)
    val expireDate: Instant? = null,
    val memberLimit: Int? = null,
    val pendingJoinRequestCount: Int? = null,
)
