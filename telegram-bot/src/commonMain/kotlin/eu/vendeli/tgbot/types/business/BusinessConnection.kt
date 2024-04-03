package eu.vendeli.tgbot.types.business

import eu.vendeli.tgbot.types.User
import kotlinx.serialization.Serializable

/**
 * Describes the connection of the bot with a business account.
 *
 * Api reference: https://core.telegram.org/bots/api#businessconnection
 * @property id Unique identifier of the business connection
 * @property user Business account user that created the business connection
 * @property userChatId Identifier of a private chat with the user who created the business connection. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a 64-bit integer or double-precision float type are safe for storing this identifier.
 * @property date Date the connection was established in Unix time
 * @property canReply True, if the bot can act on behalf of the business account in chats that were active in the last 24 hours
 * @property isEnabled True, if the connection is active
 */
@Serializable
data class BusinessConnection(
    val id: String,
    val user: User,
    val userChatId: Long,
    val date: Long,
    val canReply: Boolean,
    val isEnabled: Boolean,
)
