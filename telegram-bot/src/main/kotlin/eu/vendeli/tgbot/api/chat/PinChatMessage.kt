@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class PinChatMessageAction(messageId: Long, disableNotification: Boolean? = null) : Action<Boolean>() {
    override val method = TgMethod("pinChatMessage")
    override val returnType = getReturnType()

    init {
        parameters["message_id"] = messageId
        if (disableNotification != null) parameters["disable_notification"] = disableNotification
    }
}

fun pinChatMessage(messageId: Long, disableNotification: Boolean? = null) =
    PinChatMessageAction(messageId, disableNotification)
