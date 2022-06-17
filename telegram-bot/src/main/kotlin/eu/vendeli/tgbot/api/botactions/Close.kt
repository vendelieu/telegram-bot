package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.internal.TgMethod

class CloseAction : SimpleAction<Boolean> {
    override val method: TgMethod = TgMethod("close")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()
}

fun close() = CloseAction()
