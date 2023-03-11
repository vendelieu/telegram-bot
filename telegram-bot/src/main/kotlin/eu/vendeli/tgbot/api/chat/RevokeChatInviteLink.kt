@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.types.ChatInviteLink
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class RevokeChatInviteLinkAction(inviteLink: String) : Action<ChatInviteLink>, ActionState() {
    override val method: TgMethod = TgMethod("revokeChatInviteLink")
    override val returnType = getReturnType()

    init {
        parameters["invite_link"] = inviteLink
    }
}

fun revokeChatInviteLink(inviteLink: String) = RevokeChatInviteLinkAction(inviteLink)
