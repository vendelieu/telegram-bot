package com.github.vendelieu.tgbot.api.chat

import com.github.vendelieu.tgbot.interfaces.Action
import com.github.vendelieu.tgbot.types.ChatInviteLink
import com.github.vendelieu.tgbot.types.internal.TgMethod

class RevokeChatInviteLinkAction(inviteLink: String) : Action<ChatInviteLink> {
    override val method: TgMethod = TgMethod("revokeChatInviteLink")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["invite_link"] = inviteLink
    }
}

fun revokeChatInviteLink(inviteLink: String) = RevokeChatInviteLinkAction(inviteLink)
