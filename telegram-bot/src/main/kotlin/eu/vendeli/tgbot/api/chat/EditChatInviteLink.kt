@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.chat.ChatInviteLink
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.ChatInviteLinkOptions
import eu.vendeli.tgbot.utils.getReturnType

class EditChatInviteLinkAction(inviteLink: String) :
    Action<ChatInviteLink>,
    ActionState(),
    OptionsFeature<EditChatInviteLinkAction, ChatInviteLinkOptions> {
    override val method: TgMethod = TgMethod("editChatInviteLink")
    override val returnType = getReturnType()
    override var options = ChatInviteLinkOptions()

    init {
        parameters["invite_link"] = inviteLink
    }
}

fun editChatInviteLink(inviteLink: String) = EditChatInviteLinkAction(inviteLink)
