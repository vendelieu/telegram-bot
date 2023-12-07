@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.InlinableAction
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class StopMessageLiveLocationAction() :
    InlinableAction<Message>(),
    MarkupFeature<StopMessageLiveLocationAction> {
    override val method = TgMethod("stopMessageLiveLocation")
    override val returnType = getReturnType()

    constructor(messageId: Long) : this() {
        parameters["message_id"] = messageId
    }
}

fun stopMessageLiveLocation(messageId: Long) = StopMessageLiveLocationAction(messageId)
fun stopMessageLiveLocation() = StopMessageLiveLocationAction()
