@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod

class SetChatDescriptionAction(description: String) : Action<Boolean> {
    override val method: TgMethod = TgMethod("setChatDescription")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["description"] = description
    }
}

fun setChatDescription(title: String) = SetChatDescriptionAction(title)
