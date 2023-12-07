@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class ExportChatInviteLinkAction : Action<String>() {
    override val method = TgMethod("exportChatInviteLink")
    override val returnType = getReturnType()
}

fun exportChatInviteLink() = ExportChatInviteLinkAction()
