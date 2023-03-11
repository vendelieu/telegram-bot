@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class SetChatDescriptionAction(description: String) : Action<Boolean>, ActionState() {
    override val method: TgMethod = TgMethod("setChatDescription")
    override val returnType = getReturnType()

    init {
        parameters["description"] = description
    }
}

fun setChatDescription(title: String) = SetChatDescriptionAction(title)
