@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class ExportChatInviteLinkAction : Action<String>() {
    override val method = TgMethod("exportChatInviteLink")
    override val returnType = getReturnType()
}

/**
 * Use this method to generate a new primary invite link for a chat; any previously generated primary link is revoked. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns the new invite link as String on success.
 * @param chatId Required 
 * @returns [String]
 * Api reference: https://core.telegram.org/bots/api#exportchatinvitelink
*/
@Suppress("NOTHING_TO_INLINE")
inline fun exportChatInviteLink() = ExportChatInviteLinkAction()
