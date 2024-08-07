@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.chat.ChatInviteLink
import eu.vendeli.tgbot.types.internal.options.ChatInviteLinkOptions
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class EditChatInviteLinkAction(
    inviteLink: String,
) : Action<ChatInviteLink>(),
    OptionsFeature<EditChatInviteLinkAction, ChatInviteLinkOptions> {
    override val method = "editChatInviteLink"
    override val returnType = getReturnType()
    override val options = ChatInviteLinkOptions()

    init {
        parameters["invite_link"] = inviteLink.toJsonElement()
    }
}

/**
 * Use this method to edit a non-primary invite link created by the bot. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns the edited invite link as a ChatInviteLink object.
 *
 * [Api reference](https://core.telegram.org/bots/api#editchatinvitelink)
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param inviteLink The invite link to edit
 * @param name Invite link name; 0-32 characters
 * @param expireDate Point in time (Unix timestamp) when the link will expire
 * @param memberLimit The maximum number of users that can be members of the chat simultaneously after joining the chat via this invite link; 1-99999
 * @param createsJoinRequest True, if users joining the chat via the link need to be approved by chat administrators. If True, member_limit can't be specified
 * @returns [ChatInviteLink]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun editChatInviteLink(inviteLink: String) = EditChatInviteLinkAction(inviteLink)
