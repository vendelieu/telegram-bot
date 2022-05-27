package com.github.vendelieu.tgbot.api.chat

import com.github.vendelieu.tgbot.interfaces.Action
import com.github.vendelieu.tgbot.interfaces.features.OptionAble
import com.github.vendelieu.tgbot.interfaces.features.OptionsFeature
import com.github.vendelieu.tgbot.types.internal.TgMethod
import com.github.vendelieu.tgbot.types.internal.options.PromoteChatMemberOptions

class PromoteChatMemberAction(userId: Long) :
    Action<Boolean>,
    OptionAble,
    OptionsFeature<PromoteChatMemberAction, PromoteChatMemberOptions> {
    override val method: TgMethod = TgMethod("promoteChatMember")
    override var options = PromoteChatMemberOptions()
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["user_id"] = userId
    }
}

fun promoteChatMember(userId: Long) = PromoteChatMemberAction(userId)
