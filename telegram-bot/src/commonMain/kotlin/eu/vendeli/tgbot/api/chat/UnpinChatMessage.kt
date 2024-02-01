@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class UnpinChatMessageAction(messageId: Long) : Action<Boolean>() {
    override val method = TgMethod("unpinChatMessage")
    override val returnType = getReturnType()

    init {
        parameters["message_id"] = messageId.toJsonElement()
    }
}

inline fun unpinChatMessage(messageId: Long) = UnpinChatMessageAction(messageId)
