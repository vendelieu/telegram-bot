package eu.vendeli.tgbot.types

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import eu.vendeli.tgbot.interfaces.MultipleResponse

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "status"
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
    data class Owner(
        val user: User,
        val isAnonymous: Boolean,
        val customTitle: String? = null,
    ) : ChatMember("creator")

    data class Administrator(
        val user: User,
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
        val customTitle: String? = null,
    ) : ChatMember("administrator")

    data class Member(
        val user: User,
    ) : ChatMember("member")

    data class Restricted(
        val user: User,
        val isMember: Boolean,
        val canChangeInfo: Boolean,
        val canInviteUsers: Boolean,
        val canPinMessages: Boolean? = null,
        val canSendMessages: Boolean,
        val canSendMediaMessages: Boolean,
        val canSendPolls: Boolean,
        val canSendOtherMessages: Boolean,
        val canAddWebPagePreviews: Boolean,
        val untilDate: Int,
    ) : ChatMember("restricted")

    data class Left(
        val user: User,
    ) : ChatMember("left")

    data class Banned(
        val user: User,
        val untilDate: Int,
    ) : ChatMember("kicked")
}
