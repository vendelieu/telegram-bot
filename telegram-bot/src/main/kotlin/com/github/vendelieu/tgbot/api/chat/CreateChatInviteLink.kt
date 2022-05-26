package com.github.vendelieu.tgbot.api.chat

import com.github.vendelieu.tgbot.interfaces.Action
import com.github.vendelieu.tgbot.interfaces.features.OptionAble
import com.github.vendelieu.tgbot.interfaces.features.OptionsFeature
import com.github.vendelieu.tgbot.types.ChatInviteLink
import com.github.vendelieu.tgbot.types.internal.TgMethod
import com.github.vendelieu.tgbot.types.internal.options.ChatInviteLinkOptions

class CreateChatInviteLinkAction :
    Action<ChatInviteLink>,
    OptionAble,
    OptionsFeature<CreateChatInviteLinkAction, ChatInviteLinkOptions> {
    override val method: TgMethod = TgMethod("createChatInviteLink")
    override var options = ChatInviteLinkOptions()
    override val parameters: MutableMap<String, Any> = mutableMapOf()
}

fun createChatInviteLink() = CreateChatInviteLinkAction()
