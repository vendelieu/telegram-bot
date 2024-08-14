@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.types.chat.ChatInviteLink
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

@TgAPI
class EditChatSubscriptionInviteLinkAction(
    inviteLink: String,
    name: String? = null,
) : Action<ChatInviteLink>() {
    @TgAPI.Name("editChatSubscriptionInviteLink")
    override val method = "editChatSubscriptionInviteLink"
    override val returnType = getReturnType()

    init {
        parameters["invite_link"] = inviteLink.toJsonElement()
        if (name != null) parameters["name"] = name.toJsonElement()
    }
}

@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun editChatSubscriptionInviteLink(
    inviteLink: String,
    name: String? = null,
) = EditChatSubscriptionInviteLinkAction(inviteLink, name)
