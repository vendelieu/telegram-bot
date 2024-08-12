@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.utils.getReturnType

@TgAPI
class ExportChatInviteLinkAction : Action<String>() {
    override val method = "exportChatInviteLink"
    override val returnType = getReturnType()
}

/**
 * Use this method to generate a new primary invite link for a chat; any previously generated primary link is revoked. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns the new invite link as String on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#exportchatinvitelink)
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @returns [String]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun exportChatInviteLink() = ExportChatInviteLinkAction()
