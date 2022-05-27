package com.github.vendelieu.tgbot.api.botactions

import com.github.vendelieu.tgbot.interfaces.SimpleAction
import com.github.vendelieu.tgbot.types.User
import com.github.vendelieu.tgbot.types.internal.TgMethod

class GetMeAction : SimpleAction<User> {
    override val method: TgMethod = TgMethod("getMe")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()
}

fun getMe() = GetMeAction()
