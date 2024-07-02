package eu.vendeli.tgbot.types.chat

import eu.vendeli.tgbot.interfaces.MultipleResponse
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@Serializable
@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("status")
sealed class ChatMember(
    val status: String,
) : MultipleResponse {
    abstract val user: User

    @Serializable
    @SerialName("creator")
    data class Owner(
        override val user: User,
        val isAnonymous: Boolean,
        val customTitle: String? = null,
    ) : ChatMember("creator")

    @Serializable
    @SerialName("administrator")
    data class Administrator(
        override val user: User,
        val canBeEdited: Boolean,
        val isAnonymous: Boolean,
        val canManageChat: Boolean,
        val canDeleteMessages: Boolean,
        val canRestrictMembers: Boolean,
        val canPromoteMembers: Boolean,
        val canChangeInfo: Boolean,
        val canInviteUsers: Boolean,
        val canPostMessages: Boolean? = null,
        val canEditMessages: Boolean? = null,
        val canPinMessages: Boolean? = null,
        val canManageTopics: Boolean? = null,
        val canPostStories: Boolean? = null,
        val canEditStories: Boolean? = null,
        val canDeleteStories: Boolean? = null,
        val customTitle: String? = null,
    ) : ChatMember("administrator")

    @Serializable
    @SerialName("member")
    data class Member(
        override val user: User,
    ) : ChatMember("member")

    @Serializable
    @SerialName("restricted")
    data class Restricted(
        override val user: User,
        val isMember: Boolean,
        val canChangeInfo: Boolean,
        val canInviteUsers: Boolean,
        val canPinMessages: Boolean? = null,
        val canSendMessages: Boolean,
        val canSendAudios: Boolean,
        val canSendDocuments: Boolean,
        val canSendPhotos: Boolean,
        val canSendVideos: Boolean,
        val canSendVideoNotes: Boolean,
        val canSendVoiceNotes: Boolean,
        val canSendPolls: Boolean,
        val canSendOtherMessages: Boolean,
        val canAddWebPagePreviews: Boolean,
        val canManageTopics: Boolean? = null,
        @Serializable(InstantSerializer::class)
        val untilDate: Instant,
    ) : ChatMember("restricted")

    @Serializable
    @SerialName("left")
    data class Left(
        override val user: User,
    ) : ChatMember("left")

    @Serializable
    @SerialName("kicked")
    data class Banned(
        override val user: User,
        @Serializable(InstantSerializer::class)
        val untilDate: Instant,
    ) : ChatMember("kicked")
}
