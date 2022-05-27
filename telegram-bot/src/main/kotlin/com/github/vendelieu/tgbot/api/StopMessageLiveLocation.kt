package com.github.vendelieu.tgbot.api

import com.github.vendelieu.tgbot.interfaces.Action
import com.github.vendelieu.tgbot.interfaces.features.MarkupAble
import com.github.vendelieu.tgbot.interfaces.features.MarkupFeature
import com.github.vendelieu.tgbot.types.Message
import com.github.vendelieu.tgbot.types.internal.TgMethod

class StopMessageLiveLocationAction() : Action<Message>, MarkupAble, MarkupFeature<StopMessageLiveLocationAction> {
    override val method: TgMethod = TgMethod("stopMessageLiveLocation")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    constructor(messageId: Long) : this() {
        parameters["message_id"] = messageId
    }
}

fun stopMessageLiveLocation(messageId: Long) = StopMessageLiveLocationAction(messageId)
fun stopMessageLiveLocation() = StopMessageLiveLocationAction()
