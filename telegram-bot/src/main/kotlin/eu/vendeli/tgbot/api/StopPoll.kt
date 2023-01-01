@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.features.MarkupAble
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.types.Poll
import eu.vendeli.tgbot.types.internal.TgMethod

class StopPollAction(messageId: Long) : Action<Poll>, MarkupAble, MarkupFeature<SendPollAction> {
    override val method: TgMethod = TgMethod("stopPoll")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["message_id"] = messageId
    }
}

fun stopPoll(messageId: Long) = StopPollAction(messageId)
