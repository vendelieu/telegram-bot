@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class ExportChatInviteLinkAction : Action<String>, ActionState() {
    override val method: TgMethod = TgMethod("exportChatInviteLink")
    override val returnType = getReturnType()
}

fun exportChatInviteLink() = ExportChatInviteLinkAction()
