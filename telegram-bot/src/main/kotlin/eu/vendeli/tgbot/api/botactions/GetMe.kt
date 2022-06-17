package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.TgMethod

class GetMeAction : SimpleAction<User> {
    override val method: TgMethod = TgMethod("getMe")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()
}

fun getMe() = GetMeAction()
