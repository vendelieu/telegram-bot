package eu.vendeli.tgbot.types.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ChatType {
    @SerialName("private")
    Private,

    @SerialName("group")
    Group,

    @SerialName("supergroup")
    Supergroup,

    @SerialName("channel")
    Channel,

    @SerialName("sender")
    Sender,
}

/**
 * This object represents a chat.
 *
 * [Api reference](https://core.telegram.org/bots/api#chat)
 * @property id Unique identifier for this chat. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this identifier.
 * @property type Type of chat, can be either "private", "group", "supergroup" or "channel"
 * @property title Optional. Title, for supergroups, channels and group chats
 * @property username Optional. Username, for private chats, supergroups and channels if available
 * @property firstName Optional. First name of the other party in a private chat
 * @property lastName Optional. Last name of the other party in a private chat
 * @property isForum Optional. True, if the supergroup chat is a forum (has topics enabled)
 * @property photo Optional. Chat photo. Returned only in getChat.
 * @property activeUsernames Optional. If non-empty, the list of all active chat usernames; for private chats, supergroups and channels. Returned only in getChat.
 * @property birthdate Optional. For private chats, the date of birth of the user. Returned only in getChat.
 * @property businessIntro Optional. For private chats with business accounts, the intro of the business. Returned only in getChat.
 * @property businessLocation Optional. For private chats with business accounts, the location of the business. Returned only in getChat.
 * @property businessOpeningHours Optional. For private chats with business accounts, the opening hours of the business. Returned only in getChat.
 * @property personalChat Optional. For private chats, the personal channel of the user. Returned only in getChat.
 * @property availableReactions Optional. List of available reactions allowed in the chat. If omitted, then all emoji reactions are allowed. Returned only in getChat.
 * @property accentColorId Optional. Identifier of the accent color for the chat name and backgrounds of the chat photo, reply header, and link preview. See accent colors for more details. Returned only in getChat. Always returned in getChat.
 * @property backgroundCustomEmojiId Optional. Custom emoji identifier of emoji chosen by the chat for the reply header and link preview background. Returned only in getChat.
 * @property profileAccentColorId Optional. Identifier of the accent color for the chat's profile background. See profile accent colors for more details. Returned only in getChat.
 * @property profileBackgroundCustomEmojiId Optional. Custom emoji identifier of the emoji chosen by the chat for its profile background. Returned only in getChat.
 * @property emojiStatusCustomEmojiId Optional. Custom emoji identifier of the emoji status of the chat or the other party in a private chat. Returned only in getChat.
 * @property emojiStatusExpirationDate Optional. Expiration date of the emoji status of the chat or the other party in a private chat, in Unix time, if any. Returned only in getChat.
 * @property bio Optional. Bio of the other party in a private chat. Returned only in getChat.
 * @property hasPrivateForwards Optional. True, if privacy settings of the other party in the private chat allows to use tg://user?id=<user_id> links only in chats with the user. Returned only in getChat.
 * @property hasRestrictedVoiceAndVideoMessages Optional. True, if the privacy settings of the other party restrict sending voice and video note messages in the private chat. Returned only in getChat.
 * @property joinToSendMessages Optional. True, if users need to join the supergroup before they can send messages. Returned only in getChat.
 * @property joinByRequest Optional. True, if all users directly joining the supergroup need to be approved by supergroup administrators. Returned only in getChat.
 * @property description Optional. Description, for groups, supergroups and channel chats. Returned only in getChat.
 * @property inviteLink Optional. Primary invite link, for groups, supergroups and channel chats. Returned only in getChat.
 * @property pinnedMessage Optional. The most recent pinned message (by sending date). Returned only in getChat.
 * @property permissions Optional. Default chat member permissions, for groups and supergroups. Returned only in getChat.
 * @property slowModeDelay Optional. For supergroups, the minimum allowed delay between consecutive messages sent by each unprivileged user; in seconds. Returned only in getChat.
 * @property unrestrictBoostCount Optional. For supergroups, the minimum number of boosts that a non-administrator user needs to add in order to ignore slow mode and chat permissions. Returned only in getChat.
 * @property messageAutoDeleteTime Optional. The time after which all messages sent to the chat will be automatically deleted; in seconds. Returned only in getChat.
 * @property hasAggressiveAntiSpamEnabled Optional. True, if aggressive anti-spam checks are enabled in the supergroup. The field is only available to chat administrators. Returned only in getChat.
 * @property hasHiddenMembers Optional. True, if non-administrators can only get the list of bots and administrators in the chat. Returned only in getChat.
 * @property hasProtectedContent Optional. True, if messages from the chat can't be forwarded to other chats. Returned only in getChat.
 * @property hasVisibleHistory Optional. True, if new chat members will have access to old messages; available only to chat administrators. Returned only in getChat.
 * @property stickerSetName Optional. For supergroups, name of group sticker set. Returned only in getChat.
 * @property canSetStickerSet Optional. True, if the bot can change the group sticker set. Returned only in getChat.
 * @property customEmojiStickerSetName Optional. For supergroups, the name of the group's custom emoji sticker set. Custom emoji from this set can be used by all users and bots in the group. Returned only in getChat.
 * @property linkedChatId Optional. Unique identifier for the linked chat, i.e. the discussion group identifier for a channel and vice versa; for supergroups and channel chats. This identifier may be greater than 32 bits and some programming languages may have difficulty/silent defects in interpreting it. But it is smaller than 52 bits, so a signed 64 bit integer or double-precision float type are safe for storing this identifier. Returned only in getChat.
 * @property location Optional. For supergroups, the location to which the supergroup is connected. Returned only in getChat.
 */
@Serializable
data class Chat(
    val id: Long,
    val type: ChatType,
    val title: String? = null,
    val username: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val isForum: Boolean? = null
) {
    val fullName = (firstName?.plus(" ") ?: "") + (lastName ?: "")
}
