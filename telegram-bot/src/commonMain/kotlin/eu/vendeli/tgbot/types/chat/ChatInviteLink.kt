package eu.vendeli.tgbot.types.chat

import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.utils.serde.DurationSerializer
import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlin.time.Instant
import kotlinx.serialization.Serializable
import kotlin.time.Duration

/**
 * Represents an invite link for a chat.
 *
 * [Api reference](https://core.telegram.org/bots/api#chatinvitelink)
 * @property inviteLink The invite link. If the link was created by another chat administrator, then the second part of the link will be replaced with "...".
 * @property creator Creator of the link
 * @property createsJoinRequest True, if users joining the chat via the link need to be approved by chat administrators
 * @property isPrimary True, if the link is primary
 * @property isRevoked True, if the link is revoked
 * @property name Optional. Invite link name
 * @property expireDate Optional. Point in time (Unix timestamp) when the link will expire or has been expired
 * @property memberLimit Optional. The maximum number of users that can be members of the chat simultaneously after joining the chat via this invite link; 1-99999
 * @property pendingJoinRequestCount Optional. Number of pending join requests created using this link
 * @property subscriptionPeriod Optional. The number of seconds the subscription will be active for before the next payment
 * @property subscriptionPrice Optional. The amount of Telegram Stars a user must pay initially and after each subsequent subscription period to be a member of the chat using the link
 */
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
    @Serializable(DurationSerializer::class)
    val subscriptionPeriod: Duration? = null,
    val subscriptionPrice: Int? = null,
)
