package eu.vendeli.tgbot.types.chat

import eu.vendeli.tgbot.types.Birthdate
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.ReactionType
import eu.vendeli.tgbot.types.business.BusinessIntro
import eu.vendeli.tgbot.types.business.BusinessLocation
import eu.vendeli.tgbot.types.business.BusinessOpeningHours
import kotlinx.serialization.Serializable

/**
 * This object contains full information about a chat.
 *
 * [Api reference](https://core.telegram.org/bots/api#chatfullinfo)
 * @property id Unique identifier for this chat. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this identifier.
 * @property type Type of the chat, can be either "private", "group", "supergroup" or "channel"
 * @property title Optional. Title, for supergroups, channels and group chats
 * @property username Optional. Username, for private chats, supergroups and channels if available
 * @property firstName Optional. First name of the other party in a private chat
 * @property lastName Optional. Last name of the other party in a private chat
 * @property isForum Optional. True, if the supergroup chat is a forum (has topics enabled)
 * @property accentColorId Identifier of the accent color for the chat name and backgrounds of the chat photo, reply header, and link preview. See accent colors for more details.
 * @property maxReactionCount The maximum number of reactions that can be set on a message in the chat
 * @property photo Optional. Chat photo
 * @property activeUsernames Optional. If non-empty, the list of all active chat usernames; for private chats, supergroups and channels
 * @property birthdate Optional. For private chats, the date of birth of the user
 * @property businessIntro Optional. For private chats with business accounts, the intro of the business
 * @property businessLocation Optional. For private chats with business accounts, the location of the business
 * @property businessOpeningHours Optional. For private chats with business accounts, the opening hours of the business
 * @property personalChat Optional. For private chats, the personal channel of the user
 * @property availableReactions Optional. List of available reactions allowed in the chat. If omitted, then all emoji reactions are allowed.
 * @property backgroundCustomEmojiId Optional. Custom emoji identifier of the emoji chosen by the chat for the reply header and link preview background
 * @property profileAccentColorId Optional. Identifier of the accent color for the chat's profile background. See profile accent colors for more details.
 * @property profileBackgroundCustomEmojiId Optional. Custom emoji identifier of the emoji chosen by the chat for its profile background
 * @property emojiStatusCustomEmojiId Optional. Custom emoji identifier of the emoji status of the chat or the other party in a private chat
 * @property emojiStatusExpirationDate Optional. Expiration date of the emoji status of the chat or the other party in a private chat, in Unix time, if any
 * @property bio Optional. Bio of the other party in a private chat
 * @property hasPrivateForwards Optional. True, if privacy settings of the other party in the private chat allows to use tg://user?id=<user_id> links only in chats with the user
 * @property hasRestrictedVoiceAndVideoMessages Optional. True, if the privacy settings of the other party restrict sending voice and video note messages in the private chat
 * @property joinToSendMessages Optional. True, if users need to join the supergroup before they can send messages
 * @property joinByRequest Optional. True, if all users directly joining the supergroup without using an invite link need to be approved by supergroup administrators
 * @property description Optional. Description, for groups, supergroups and channel chats
 * @property inviteLink Optional. Primary invite link, for groups, supergroups and channel chats
 * @property pinnedMessage Optional. The most recent pinned message (by sending date)
 * @property permissions Optional. Default chat member permissions, for groups and supergroups
 * @property canSendPaidMedia Optional. True, if paid media messages can be sent or forwarded to the channel chat. The field is available only for channel chats.
 * @property slowModeDelay Optional. For supergroups, the minimum allowed delay between consecutive messages sent by each unprivileged user; in seconds
 * @property unrestrictBoostCount Optional. For supergroups, the minimum number of boosts that a non-administrator user needs to add in order to ignore slow mode and chat permissions
 * @property messageAutoDeleteTime Optional. The time after which all messages sent to the chat will be automatically deleted; in seconds
 * @property hasAggressiveAntiSpamEnabled Optional. True, if aggressive anti-spam checks are enabled in the supergroup. The field is only available to chat administrators.
 * @property hasHiddenMembers Optional. True, if non-administrators can only get the list of bots and administrators in the chat
 * @property hasProtectedContent Optional. True, if messages from the chat can't be forwarded to other chats
 * @property hasVisibleHistory Optional. True, if new chat members will have access to old messages; available only to chat administrators
 * @property stickerSetName Optional. For supergroups, name of the group sticker set
 * @property canSetStickerSet Optional. True, if the bot can change the group sticker set
 * @property customEmojiStickerSetName Optional. For supergroups, the name of the group's custom emoji sticker set. Custom emoji from this set can be used by all users and bots in the group.
 * @property linkedChatId Optional. Unique identifier for the linked chat, i.e. the discussion group identifier for a channel and vice versa; for supergroups and channel chats. This identifier may be greater than 32 bits and some programming languages may have difficulty/silent defects in interpreting it. But it is smaller than 52 bits, so a signed 64 bit integer or double-precision float type are safe for storing this identifier.
 * @property location Optional. For supergroups, the location to which the supergroup is connected
 */
@Serializable
data class ChatFullInfo(
    val id: Long,
    val type: ChatType,
    val title: String? = null,
    val username: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val isForum: Boolean? = null,
    val accentColorId: Int? = null,
    val maxReactionCount: Int? = null,
    val photo: ChatPhoto? = null,
    val activeUsernames: List<String>? = null,
    val birthdate: Birthdate? = null,
    val businessIntro: BusinessIntro? = null,
    val businessLocation: BusinessLocation? = null,
    val businessOpeningHours: BusinessOpeningHours? = null,
    val personalChat: Chat? = null,
    val availableReactions: List<ReactionType>? = null,
    val backgroundCustomEmojiId: String? = null,
    val profileAccentColorId: Int? = null,
    val profileBackgroundCustomEmojiId: String? = null,
    val emojiStatusCustomEmojiId: String? = null,
    val emojiStatusExpirationDate: Int? = null,
    val bio: String? = null,
    val hasPrivateForwards: Boolean? = null,
    val hasRestrictedVoiceAndVideoMessages: Boolean? = null,
    val joinToSendMessages: Boolean? = null,
    val joinByRequest: Boolean? = null,
    val description: String? = null,
    val inviteLink: String? = null,
    val pinnedMessage: Message? = null,
    val permissions: ChatPermissions? = null,
    val canSendPaidMedia: Boolean? = null,
    val slowModeDelay: Int? = null,
    val unrestrictBoostCount: Int? = null,
    val messageAutoDeleteTime: Int? = null,
    val hasAggressiveAntiSpamEnabled: Boolean? = null,
    val hasHiddenMembers: Boolean? = null,
    val hasProtectedContent: Boolean? = null,
    val hasVisibleHistory: Boolean? = null,
    val stickerSetName: String? = null,
    val canSetStickerSet: Boolean? = null,
    val customEmojiStickerSetName: String? = null,
    val linkedChatId: Long? = null,
    val location: ChatLocation? = null,
)
