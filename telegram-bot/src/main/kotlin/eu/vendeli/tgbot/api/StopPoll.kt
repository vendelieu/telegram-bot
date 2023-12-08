@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.types.Poll
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class StopPollAction(messageId: Long) : Action<Poll>(), MarkupFeature<SendPollAction> {
    override val method = TgMethod("stopPoll")
    override val returnType = getReturnType()

    init {
        parameters["message_id"] = messageId
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun stopPoll(messageId: Long) = StopPollAction(messageId)
