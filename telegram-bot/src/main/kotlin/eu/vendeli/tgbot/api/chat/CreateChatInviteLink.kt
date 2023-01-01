@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.features.OptionAble
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.ChatInviteLink
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.ChatInviteLinkOptions

class CreateChatInviteLinkAction :
    Action<ChatInviteLink>,
    OptionAble,
    OptionsFeature<CreateChatInviteLinkAction, ChatInviteLinkOptions> {
    override val method: TgMethod = TgMethod("createChatInviteLink")
    override var options = ChatInviteLinkOptions()
    override val parameters: MutableMap<String, Any?> = mutableMapOf()
}

fun createChatInviteLink() = CreateChatInviteLinkAction()
