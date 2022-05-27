package com.github.vendelieu.tgbot.api.chat

import com.github.vendelieu.tgbot.interfaces.Action
import com.github.vendelieu.tgbot.types.internal.TgMethod

class SetChatAdministratorCustomTitleAction(userId: Long, customTitle: String) : Action<Boolean> {
    override val method: TgMethod = TgMethod("setChatAdministratorCustomTitle")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["user_id"] = userId
        parameters["custom_title"] = customTitle
    }
}

fun setChatAdministratorCustomTitle(userId: Long, customTitle: String) =
    SetChatAdministratorCustomTitleAction(userId, customTitle)
