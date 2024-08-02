package eu.vendeli.tgbot.types

import kotlinx.serialization.Serializable

/**
 * This object represents a Telegram user or bot.
 *
 * [Api reference](https://core.telegram.org/bots/api#user)
 * @property id Unique identifier for this user or bot. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a 64-bit integer or double-precision float type are safe for storing this identifier.
 * @property isBot True, if this user is a bot
 * @property firstName User's or bot's first name
 * @property lastName Optional. User's or bot's last name
 * @property username Optional. User's or bot's username
 * @property languageCode Optional. IETF language tag of the user's language
 * @property isPremium Optional. True, if this user is a Telegram Premium user
 * @property addedToAttachmentMenu Optional. True, if this user added the bot to the attachment menu
 * @property canJoinGroups Optional. True, if the bot can be invited to groups. Returned only in getMe.
 * @property canReadAllGroupMessages Optional. True, if privacy mode is disabled for the bot. Returned only in getMe.
 * @property supportsInlineQueries Optional. True, if the bot supports inline queries. Returned only in getMe.
 * @property canConnectToBusiness Optional. True, if the bot can be connected to a Telegram Business account to receive its messages. Returned only in getMe.
 */
@Serializable
data class User(
    val id: Long,
    val isBot: Boolean,
    val firstName: String,
    val lastName: String? = null,
    val username: String? = null,
    val languageCode: String? = null,
    val isPremium: Boolean? = null,
    val addedToAttachmentMenu: Boolean? = null,
    val canJoinGroups: Boolean? = null,
    val canReadAllGroupMessages: Boolean? = null,
    val supportsInlineQueries: Boolean? = null,
    val canConnectToBusiness: Boolean? = null,
    val hasMainWebApp: Boolean? = null,
)
