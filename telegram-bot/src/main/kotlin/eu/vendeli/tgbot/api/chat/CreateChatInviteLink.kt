@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.chat.ChatInviteLink
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.ChatInviteLinkOptions
import eu.vendeli.tgbot.utils.getReturnType

class CreateChatInviteLinkAction :
    Action<ChatInviteLink>,
    ActionState(),
    OptionsFeature<CreateChatInviteLinkAction, ChatInviteLinkOptions> {
    override val method: TgMethod = TgMethod("createChatInviteLink")
    override val returnType = getReturnType()
    override val OptionsFeature<CreateChatInviteLinkAction, ChatInviteLinkOptions>.options: ChatInviteLinkOptions
        get() = ChatInviteLinkOptions()
}

fun createChatInviteLink() = CreateChatInviteLinkAction()
