@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.chat.ChatInviteLink
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class RevokeChatInviteLinkAction(inviteLink: String) : Action<ChatInviteLink>() {
    override val method = TgMethod("revokeChatInviteLink")
    override val returnType = getReturnType()

    init {
        parameters["invite_link"] = inviteLink.toJsonElement()
    }
}

/**
 * Use this method to revoke an invite link created by the bot. If the primary link is revoked, a new link is automatically generated. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns the revoked invite link as ChatInviteLink object.
 * @param chatId Required 
 * @param inviteLink Required 
 * @returns [ChatInviteLink]
 * Api reference: https://core.telegram.org/bots/api#revokechatinvitelink
*/
@Suppress("NOTHING_TO_INLINE")
inline fun revokeChatInviteLink(inviteLink: String) = RevokeChatInviteLinkAction(inviteLink)
