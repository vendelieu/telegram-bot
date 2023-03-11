@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class SetChatTitleAction(title: String) : Action<Boolean>, ActionState() {
    override val method: TgMethod = TgMethod("setChatTitle")
    override val returnType = getReturnType()

    init {
        parameters["title"] = title
    }
}

fun setChatTitle(title: String) = SetChatTitleAction(title)
