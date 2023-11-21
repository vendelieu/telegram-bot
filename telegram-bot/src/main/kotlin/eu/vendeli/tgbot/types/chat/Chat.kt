package eu.vendeli.tgbot.types.chat

import eu.vendeli.tgbot.types.Message

enum class ChatType(private val literal: String) {
    Private("private"),
    Group("group"),
    Supergroup("supergroup"),
    Channel("channel"),
    Sender("sender"),
    ;

    override fun toString(): String = literal
}

/**
 * Chat
 *
 * @property id
 * @property type
 * @property title
 * @property username
 * @property firstName
 * @property lastName
 * @property photo
 * @property activeUsernames If non-empty, the list of all active chat usernames;
 * for private chats, supergroups and channels.
 * Returned only in [getChat].
 * @property emojiStatusCustomEmojiId Custom emoji identifier of emoji status of the other party in a private chat.
 * Returned only in [getChat].
 * @property bio
 * @property hasPrivateForwards
 * @property hasRestrictedVoiceAndVideoMessages
 * @property joinToSendMessages True, if users need to join the supergroup before they can send messages.
 * Returned only in getChat.
 * @property joinByRequest True, if all users directly joining the supergroup need to be approved by supergroup
 * administrators. Returned only in getChat.
 * @property description
 * @property inviteLink
 * @property pinnedMessage
 * @property permissions
 * @property slowModeDelay
 * @property messageAutoDeleteTime
 * @property hasProtectedContent
 * @property stickerSetName
 * @property canSetStickerSet
 * @property linkedChatId
 * @property location
 * @constructor Create empty Chat
 */
data class Chat(
    val id: Long,
    val type: ChatType,
    val title: String? = null,
    val username: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val isForum: Boolean? = null,
    val photo: ChatPhoto? = null,
    val activeUsernames: List<String>? = null,
    val emojiStatusCustomEmojiId: String? = null,
    val emojiStatusExpirationDate: Long? = null,
    val bio: String? = null,
    val hasPrivateForwards: Boolean? = null,
    val hasRestrictedVoiceAndVideoMessages: Boolean? = null,
    val joinToSendMessages: Boolean? = null,
    val joinByRequest: Boolean? = null,
    val description: String? = null,
    val inviteLink: String? = null,
    val pinnedMessage: Message? = null,
    val permissions: ChatPermissions? = null,
    val slowModeDelay: Int? = null,
    val messageAutoDeleteTime: Int? = null,
    val hasProtectedContent: Boolean? = null,
    val stickerSetName: String? = null,
    val canSetStickerSet: Boolean? = null,
    val linkedChatId: Long? = null,
    val location: ChatLocation? = null,
    val hasHiddenMembers: Boolean? = null,
    val hasAggressiveAntiSpamEnabled: Boolean? = null,
)
