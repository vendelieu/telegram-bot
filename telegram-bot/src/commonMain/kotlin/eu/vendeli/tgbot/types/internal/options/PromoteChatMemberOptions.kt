package eu.vendeli.tgbot.types.internal.options

import kotlinx.serialization.Serializable

@Serializable
data class PromoteChatMemberOptions(
    var isAnonymous: Boolean? = null,
    var canManageChat: Boolean? = null,
    var canPostMessages: Boolean? = null,
    var canEditMessages: Boolean? = null,
    var canDeleteMessages: Boolean? = null,
    var canManageVideoChats: Boolean? = null,
    var canRestrictMembers: Boolean? = null,
    var canPromoteMembers: Boolean? = null,
    var canChangeInfo: Boolean? = null,
    var canInviteUsers: Boolean? = null,
    var canPinMessages: Boolean? = null,
    var canManageTopics: Boolean? = null,
    var canPostStories: Boolean? = null,
    var canEditStories: Boolean? = null,
    var canDeleteStories: Boolean? = null,
) : Options
