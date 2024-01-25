package eu.vendeli.tgbot.types.chat

import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.ReactionType
import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlinx.datetime.Instant
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

@Serializable
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
    @Serializable(InstantSerializer::class)
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
