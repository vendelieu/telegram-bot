@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.types.chat.ChatInviteLink
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class RevokeChatInviteLinkAction(
    inviteLink: String,
) : Action<ChatInviteLink>() {
    override val method = TgMethod("revokeChatInviteLink")
    override val returnType = getReturnType()

    init {
        parameters["invite_link"] = inviteLink.toJsonElement()
    }
}

/**
 * Use this method to revoke an invite link created by the bot. If the primary link is revoked, a new link is automatically generated. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns the revoked invite link as ChatInviteLink object.
 *
 * [Api reference](https://core.telegram.org/bots/api#revokechatinvitelink)
 * @param chatId Unique identifier of the target chat or username of the target channel (in the format @channelusername)
 * @param inviteLink The invite link to revoke
 * @returns [ChatInviteLink]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun revokeChatInviteLink(inviteLink: String) = RevokeChatInviteLinkAction(inviteLink)
