package eu.vendeli.tgbot.types.chat

import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

/**
 * Represents a join request sent to a chat.
 *
 * Api reference: https://core.telegram.org/bots/api#chatjoinrequest
 * @property chat Chat to which the request was sent
 * @property from User that sent the join request
 * @property userChatId Identifier of a private chat with the user who sent the join request. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a 64-bit integer or double-precision float type are safe for storing this identifier. The bot can use this identifier for 5 minutes to send messages until the join request is processed, assuming no other administrator contacted the user.
 * @property date Date the request was sent in Unix time
 * @property bio Optional. Bio of the user.
 * @property inviteLink Optional. Chat invite link that was used by the user to send the join request
 */
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
