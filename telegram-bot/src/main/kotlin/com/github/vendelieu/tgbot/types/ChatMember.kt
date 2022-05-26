package com.github.vendelieu.tgbot.types

import com.github.vendelieu.tgbot.interfaces.MultipleResponse

sealed class ChatMember : MultipleResponse {
    data class Owner(
        val status: String,
        val user: User,
        val isAnonymous: Boolean,
        val customTitle: String? = null,
    ) : ChatMember()

    data class Administrator(
        val status: String,
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
    ) : ChatMember()

    data class Member(
        val status: String,
        val user: User,
    ) : ChatMember()

    data class Restricted(
        val status: String,
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
    ) : ChatMember()

    data class Left(
        val status: String,
        val user: User,
    ) : ChatMember()

    data class Banned(
        val status: String,
        val user: User,
        val untilDate: Int,
    ) : ChatMember()
}
