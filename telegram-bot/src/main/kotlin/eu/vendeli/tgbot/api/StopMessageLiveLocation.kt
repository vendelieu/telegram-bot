@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.InlineMode
import eu.vendeli.tgbot.interfaces.features.MarkupAble
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod

class StopMessageLiveLocationAction() :
    Action<Message>,
    InlineMode<Message>,
    MarkupAble,
    MarkupFeature<StopMessageLiveLocationAction> {
    override val method: TgMethod = TgMethod("stopMessageLiveLocation")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    constructor(messageId: Long) : this() {
        parameters["message_id"] = messageId
    }
}

fun stopMessageLiveLocation(messageId: Long) = StopMessageLiveLocationAction(messageId)
fun stopMessageLiveLocation() = StopMessageLiveLocationAction()
