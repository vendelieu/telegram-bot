package com.github.vendelieu.tgbot.api

import com.github.vendelieu.tgbot.interfaces.Action
import com.github.vendelieu.tgbot.interfaces.features.MarkupAble
import com.github.vendelieu.tgbot.interfaces.features.MarkupFeature
import com.github.vendelieu.tgbot.types.Poll
import com.github.vendelieu.tgbot.types.internal.TgMethod

class StopPollAction(messageId: Long) : Action<Poll>, MarkupAble, MarkupFeature<SendPollAction> {
    override val method: TgMethod = TgMethod("stopPoll")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["message_id"] = messageId
    }
}

fun stopPoll(messageId: Long) = StopPollAction(messageId)
