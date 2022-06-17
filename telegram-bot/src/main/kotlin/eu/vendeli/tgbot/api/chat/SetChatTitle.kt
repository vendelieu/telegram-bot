package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod

class SetChatTitleAction(title: String) : Action<Boolean> {
    override val method: TgMethod = TgMethod("setChatTitle")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["title"] = title
    }
}

fun setChatTitle(title: String) = SetChatTitleAction(title)
