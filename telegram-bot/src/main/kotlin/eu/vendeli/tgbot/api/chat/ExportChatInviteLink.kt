@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class ExportChatInviteLinkAction : Action<String>, ActionState() {
    override val TgAction<String>.method: TgMethod
        get() = TgMethod("exportChatInviteLink")
    override val TgAction<String>.returnType: Class<String>
        get() = getReturnType()
}

fun exportChatInviteLink() = ExportChatInviteLinkAction()
