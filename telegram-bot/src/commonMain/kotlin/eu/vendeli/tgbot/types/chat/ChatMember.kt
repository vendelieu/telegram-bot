package eu.vendeli.tgbot.types.chat

import eu.vendeli.tgbot.interfaces.marker.MultipleResponse
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlin.time.Instant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.serializer

@Serializable
@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("status")
sealed class ChatMember : MultipleResponse {
    abstract val user: User

    @OptIn(InternalSerializationApi::class)
    val status: String by lazy {
        this::class.serializer().descriptor.serialName
    }

    @Serializable
    @SerialName("creator")
    data class Owner(
        override val user: User,
        val isAnonymous: Boolean,
        val customTitle: String? = null,
    ) : ChatMember()

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
        val canManageVideoChats: Boolean,
        val canManageTopics: Boolean? = null,
        val canPostStories: Boolean,
        val canEditStories: Boolean,
        val canDeleteStories: Boolean,
        val customTitle: String? = null,
        val canManageDirectMessages: Boolean? = null,
        val canManageTags: Boolean? = null,
    ) : ChatMember()

    @Serializable
    @SerialName("member")
    data class Member(
        override val user: User,
        val tag: String? = null,
        @Serializable(InstantSerializer::class)
        val untilDate: Instant? = null,
    ) : ChatMember()

    @Serializable
    @SerialName("restricted")
    data class Restricted(
        override val user: User,
        val tag: String? = null,
        val isMember: Boolean,
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
        val canEditTag: Boolean = false,
        val canChangeInfo: Boolean,
        val canInviteUsers: Boolean,
        val canPinMessages: Boolean,
        val canManageTopics: Boolean,
        @Serializable(InstantSerializer::class)
        val untilDate: Instant,
    ) : ChatMember()

    @Serializable
    @SerialName("left")
    data class Left(
        override val user: User,
    ) : ChatMember()

    @Serializable
    @SerialName("kicked")
    data class Banned(
        override val user: User,
        @Serializable(InstantSerializer::class)
        val untilDate: Instant,
    ) : ChatMember()
}
