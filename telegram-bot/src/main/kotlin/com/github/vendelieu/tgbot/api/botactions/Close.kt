package com.github.vendelieu.tgbot.api.botactions

import com.github.vendelieu.tgbot.interfaces.SimpleAction
import com.github.vendelieu.tgbot.types.internal.TgMethod

class CloseAction : SimpleAction<Boolean> {
    override val method: TgMethod = TgMethod("close")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()
}

fun close() = CloseAction()
