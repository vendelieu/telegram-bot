@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.ChatInviteLink
import eu.vendeli.tgbot.types.internal.TgMethod

class RevokeChatInviteLinkAction(inviteLink: String) : Action<ChatInviteLink> {
    override val method: TgMethod = TgMethod("revokeChatInviteLink")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["invite_link"] = inviteLink
    }
}

fun revokeChatInviteLink(inviteLink: String) = RevokeChatInviteLinkAction(inviteLink)
