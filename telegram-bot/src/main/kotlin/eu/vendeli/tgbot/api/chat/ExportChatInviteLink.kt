@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod

class ExportChatInviteLinkAction : Action<String> {
    override val method: TgMethod = TgMethod("exportChatInviteLink")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()
}

fun exportChatInviteLink() = ExportChatInviteLinkAction()
