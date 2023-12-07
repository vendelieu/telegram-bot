@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.PollOptions
import eu.vendeli.tgbot.utils.getReturnType

class SendPollAction(question: String, pollOptions: List<String>) :
    Action<Message>(),
    OptionsFeature<SendPollAction, PollOptions>,
    MarkupFeature<SendPollAction> {
    override val method = TgMethod("sendPoll")
    override val returnType = getReturnType()
    override val OptionsFeature<SendPollAction, PollOptions>.options: PollOptions
        get() = PollOptions()

    init {
        parameters["question"] = question
        parameters["options"] = pollOptions
    }
}

fun sendPoll(question: String, options: () -> List<String>) = poll(question, options)
fun poll(question: String, options: () -> List<String>) = SendPollAction(question, options())
fun poll(question: String, vararg options: String) = SendPollAction(question, options.toList())
