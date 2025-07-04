@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.types.chat.ChatInviteLink
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

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

/**
 * Use this method to edit a subscription invite link created by the bot. The bot must have the can_invite_users administrator rights. Returns the edited invite link as a ChatInviteLink object.
 *
 * [Api reference](https://core.telegram.org/bots/api#editchatsubscriptioninvitelink)
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param inviteLink The invite link to edit
 * @param name Invite link name; 0-32 characters
 * @returns [ChatInviteLink]
 */
@TgAPI
inline fun editChatSubscriptionInviteLink(
    inviteLink: String,
    name: String? = null,
) = EditChatSubscriptionInviteLinkAction(inviteLink, name)
