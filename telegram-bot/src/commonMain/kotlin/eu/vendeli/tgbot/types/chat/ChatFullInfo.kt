package eu.vendeli.tgbot.types.chat

import eu.vendeli.tgbot.types.Birthdate
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.ReactionType
import eu.vendeli.tgbot.types.business.BusinessIntro
import eu.vendeli.tgbot.types.business.BusinessLocation
import eu.vendeli.tgbot.types.business.BusinessOpeningHours
import kotlinx.serialization.Serializable

@Serializable
data class ChatFullInfo(
    val id: Long,
    val type: String,
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
    val location: ChatLocation? = null
)
