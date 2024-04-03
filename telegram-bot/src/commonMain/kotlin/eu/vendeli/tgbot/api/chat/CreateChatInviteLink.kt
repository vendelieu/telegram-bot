@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.chat.ChatInviteLink
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.ChatInviteLinkOptions
import eu.vendeli.tgbot.utils.getReturnType

class CreateChatInviteLinkAction :
    Action<ChatInviteLink>(),
    OptionsFeature<CreateChatInviteLinkAction, ChatInviteLinkOptions> {
    override val method = TgMethod("createChatInviteLink")
    override val returnType = getReturnType()
    override val options = ChatInviteLinkOptions()
}

/**
 * Use this method to create an additional invite link for a chat. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. The link can be revoked using the method revokeChatInviteLink. Returns the new invite link as ChatInviteLink object.
 *
 * Api reference: https://core.telegram.org/bots/api#createchatinvitelink
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param name Invite link name; 0-32 characters
 * @param expireDate Point in time (Unix timestamp) when the link will expire
 * @param memberLimit The maximum number of users that can be members of the chat simultaneously after joining the chat via this invite link; 1-99999
 * @param createsJoinRequest True, if users joining the chat via the link need to be approved by chat administrators. If True, member_limit can't be specified
 * @returns [ChatInviteLink]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun createChatInviteLink() = CreateChatInviteLinkAction()
