package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.internal.TgMethod

class LogoutAction : SimpleAction<Boolean> {
    override val method: TgMethod = TgMethod("logOut")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()
}

fun logout() = LogoutAction()
