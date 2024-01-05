package eu.vendeli.tgbot.types.chat

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import eu.vendeli.tgbot.interfaces.MultipleResponse
import eu.vendeli.tgbot.types.User
import java.time.Instant

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "status",
)
@JsonSubTypes(
    JsonSubTypes.Type(value = ChatMember.Owner::class, name = "creator"),
    JsonSubTypes.Type(value = ChatMember.Administrator::class, name = "administrator"),
    JsonSubTypes.Type(value = ChatMember.Member::class, name = "member"),
    JsonSubTypes.Type(value = ChatMember.Restricted::class, name = "restricted"),
    JsonSubTypes.Type(value = ChatMember.Left::class, name = "left"),
    JsonSubTypes.Type(value = ChatMember.Banned::class, name = "kicked"),
)
sealed class ChatMember(val status: String) : MultipleResponse {
    abstract val user: User
    data class Owner(
        override val user: User,
        val isAnonymous: Boolean,
        val customTitle: String? = null,
    ) : ChatMember("creator")

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

    data class Member(
        override val user: User,
    ) : ChatMember("member")

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
        val untilDate: Instant,
    ) : ChatMember("restricted")

    data class Left(
        override val user: User,
    ) : ChatMember("left")

    data class Banned(
        override val user: User,
        val untilDate: Instant,
    ) : ChatMember("kicked")
}
