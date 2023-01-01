@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.features.OptionAble
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.ChatInviteLink
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.ChatInviteLinkOptions

class EditChatInviteLinkAction(inviteLink: String) :
    Action<ChatInviteLink>,
    OptionAble,
    OptionsFeature<EditChatInviteLinkAction, ChatInviteLinkOptions> {
    override val method: TgMethod = TgMethod("editChatInviteLink")
    override var options = ChatInviteLinkOptions()
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["invite_link"] = inviteLink
    }
}

fun editChatInviteLink(inviteLink: String) = EditChatInviteLinkAction(inviteLink)
