@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.PollOptions
import eu.vendeli.tgbot.utils.getReturnType

class SendPollAction(question: String, pollOptions: Array<String>) :
    Action<Message>,
    ActionState(),
    OptionsFeature<SendPollAction, PollOptions>,
    MarkupFeature<SendPollAction> {
    override val TgAction<Message>.method: TgMethod
        get() = TgMethod("sendPoll")
    override val TgAction<Message>.returnType: Class<Message>
        get() = getReturnType()
    override val OptionsFeature<SendPollAction, PollOptions>.options: PollOptions
        get() = PollOptions()

    init {
        parameters["question"] = question
        parameters["options"] = pollOptions
    }
}

fun poll(question: String, options: () -> Array<String>) = SendPollAction(question, options())
fun poll(question: String, vararg options: String) = SendPollAction(question, arrayOf(*options))
