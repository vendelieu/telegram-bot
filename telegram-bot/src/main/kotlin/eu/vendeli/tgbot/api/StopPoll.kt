@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.types.Poll
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.utils.getReturnType

class StopPollAction(messageId: Long) : Action<Poll>, MarkupFeature<SendPollAction>, ActionState() {
    override val TgAction<Poll>.method: TgMethod
        get() = TgMethod("stopPoll")
    override val TgAction<Poll>.returnType: Class<Poll>
        get() = getReturnType()

    init {
        parameters["message_id"] = messageId
    }
}

fun stopPoll(messageId: Long) = StopPollAction(messageId)
