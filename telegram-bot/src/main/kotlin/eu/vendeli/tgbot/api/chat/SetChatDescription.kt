@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class SetChatDescriptionAction(description: String? = null) : Action<Boolean>() {
    override val method = TgMethod("setChatDescription")
    override val returnType = getReturnType()

    init {
        parameters["description"] = description
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun setChatDescription(title: String? = null) = SetChatDescriptionAction(title)
