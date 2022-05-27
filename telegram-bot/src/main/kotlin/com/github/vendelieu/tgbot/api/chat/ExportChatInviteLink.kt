package com.github.vendelieu.tgbot.api.chat

import com.github.vendelieu.tgbot.interfaces.Action
import com.github.vendelieu.tgbot.types.internal.TgMethod

class ExportChatInviteLinkAction : Action<String> {
    override val method: TgMethod = TgMethod("exportChatInviteLink")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()
}

fun exportChatInviteLink() = ExportChatInviteLinkAction()
