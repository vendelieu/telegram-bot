package eu.vendeli.tgbot.types.chat

import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.ReactionType
import java.time.Instant

enum class ChatType(private val literal: String) {
    Private("private"),
    Group("group"),
    Supergroup("supergroup"),
    Channel("channel"),
    Sender("sender"),
    ;

    override fun toString(): String = literal
}

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
    val availableReactions: List<ReactionType>? = null,
    val accentColorId: Int? = null,
    val backgroundCustomEmojiId: String? = null,
    val profileAccentColorId: Int? = null,
    val profileBackgroundCustomEmojiId: String? = null,
    val emojiStatusCustomEmojiId: String? = null,
    val emojiStatusExpirationDate: Instant? = null,
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
    val hasVisibleHistory: Boolean? = null,
    val stickerSetName: String? = null,
    val canSetStickerSet: Boolean? = null,
    val linkedChatId: Long? = null,
    val location: ChatLocation? = null,
    val hasHiddenMembers: Boolean? = null,
    val hasAggressiveAntiSpamEnabled: Boolean? = null,
)
