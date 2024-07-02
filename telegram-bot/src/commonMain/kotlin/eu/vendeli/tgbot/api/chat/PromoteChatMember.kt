@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.PromoteChatMemberOptions
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class PromoteChatMemberAction(
    userId: Long,
) : Action<Boolean>(),
    OptionsFeature<PromoteChatMemberAction, PromoteChatMemberOptions> {
    override val method = TgMethod("promoteChatMember")
    override val returnType = getReturnType()
    override val options = PromoteChatMemberOptions()

    init {
        parameters["user_id"] = userId.toJsonElement()
    }
}

/**
 * Use this method to promote or demote a user in a supergroup or a channel. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Pass False for all boolean parameters to demote a user. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#promotechatmember)
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param userId Unique identifier of the target user
 * @param isAnonymous Pass True if the administrator's presence in the chat is hidden
 * @param canManageChat Pass True if the administrator can access the chat event log, get boost list, see hidden supergroup and channel members, report spam messages and ignore slow mode. Implied by any other administrator privilege.
 * @param canDeleteMessages Pass True if the administrator can delete messages of other users
 * @param canManageVideoChats Pass True if the administrator can manage video chats
 * @param canRestrictMembers Pass True if the administrator can restrict, ban or unban chat members, or access supergroup statistics
 * @param canPromoteMembers Pass True if the administrator can add new administrators with a subset of their own privileges or demote administrators that they have promoted, directly or indirectly (promoted by administrators that were appointed by him)
 * @param canChangeInfo Pass True if the administrator can change chat title, photo and other settings
 * @param canInviteUsers Pass True if the administrator can invite new users to the chat
 * @param canPostStories Pass True if the administrator can post stories to the chat
 * @param canEditStories Pass True if the administrator can edit stories posted by other users, post stories to the chat page, pin chat stories, and access the chat's story archive
 * @param canDeleteStories Pass True if the administrator can delete stories posted by other users
 * @param canPostMessages Pass True if the administrator can post messages in the channel, or access channel statistics; for channels only
 * @param canEditMessages Pass True if the administrator can edit messages of other users and can pin messages; for channels only
 * @param canPinMessages Pass True if the administrator can pin messages; for supergroups only
 * @param canManageTopics Pass True if the user is allowed to create, rename, close, and reopen forum topics; for supergroups only
 * @returns [Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun promoteChatMember(userId: Long) = PromoteChatMemberAction(userId)

@Suppress("NOTHING_TO_INLINE")
inline fun promoteChatMember(user: User) = promoteChatMember(user.id)
