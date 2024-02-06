@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.chat.ChatInviteLink
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.ChatInviteLinkOptions
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class EditChatInviteLinkAction(inviteLink: String) :
    Action<ChatInviteLink>(),
    OptionsFeature<EditChatInviteLinkAction, ChatInviteLinkOptions> {
    override val method = TgMethod("editChatInviteLink")
    override val returnType = getReturnType()
    override val options = ChatInviteLinkOptions()

    init {
        parameters["invite_link"] = inviteLink.toJsonElement()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun editChatInviteLink(inviteLink: String) = EditChatInviteLinkAction(inviteLink)
