package eu.vendeli.tgbot.types.chat

import kotlinx.serialization.Serializable

/**
 * Represents the rights of an administrator in a chat.
 * Api reference: https://core.telegram.org/bots/api#chatadministratorrights
 * @property isAnonymous True, if the user's presence in the chat is hidden
 * @property canManageChat True, if the administrator can access the chat event log, get boost list, see hidden supergroup and channel members, report spam messages and ignore slow mode. Implied by any other administrator privilege.
 * @property canDeleteMessages True, if the administrator can delete messages of other users
 * @property canManageVideoChats True, if the administrator can manage video chats
 * @property canRestrictMembers True, if the administrator can restrict, ban or unban chat members, or access supergroup statistics
 * @property canPromoteMembers True, if the administrator can add new administrators with a subset of their own privileges or demote administrators that they have promoted, directly or indirectly (promoted by administrators that were appointed by the user)
 * @property canChangeInfo True, if the user is allowed to change the chat title, photo and other settings
 * @property canInviteUsers True, if the user is allowed to invite new users to the chat
 * @property canPostStories True, if the administrator can post stories to the chat
 * @property canEditStories True, if the administrator can edit stories posted by other users
 * @property canDeleteStories True, if the administrator can delete stories posted by other users
 * @property canPostMessages Optional. True, if the administrator can post messages in the channel, or access channel statistics; channels only
 * @property canEditMessages Optional. True, if the administrator can edit messages of other users and can pin messages; channels only
 * @property canPinMessages Optional. True, if the user is allowed to pin messages; groups and supergroups only
 * @property canManageTopics Optional. True, if the user is allowed to create, rename, close, and reopen forum topics; supergroups only
*/
@Serializable
data class ChatAdministratorRights(
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
)
