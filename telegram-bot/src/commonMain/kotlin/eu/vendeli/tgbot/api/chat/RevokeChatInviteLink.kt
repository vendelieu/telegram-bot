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

@Suppress("NOTHING_TO_INLINE")
inline fun revokeChatInviteLink(inviteLink: String) = RevokeChatInviteLinkAction(inviteLink)
