package com.github.vendelieu.tgbot.api.botactions

import com.github.vendelieu.tgbot.interfaces.SimpleAction
import com.github.vendelieu.tgbot.types.internal.TgMethod

class LogoutAction : SimpleAction<Boolean> {
    override val method: TgMethod = TgMethod("logOut")
    override val parameters: MutableMap<String, Any> = mutableMapOf()
}

fun logout() = LogoutAction()
