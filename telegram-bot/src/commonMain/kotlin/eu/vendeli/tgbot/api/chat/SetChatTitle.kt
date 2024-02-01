@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class SetChatTitleAction(title: String) : Action<Boolean>() {
    override val method = TgMethod("setChatTitle")
    override val returnType = getReturnType()

    init {
        parameters["title"] = title.toJsonElement()
    }
}

inline fun setChatTitle(title: String) = SetChatTitleAction(title)
