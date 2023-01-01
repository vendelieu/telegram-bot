@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.features.MarkupAble
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionAble
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.PollOptions

class SendPollAction(question: String, pollOptions: Array<String>) :
    Action<Message>,
    OptionAble,
    MarkupAble,
    OptionsFeature<SendPollAction, PollOptions>,
    MarkupFeature<SendPollAction> {
    override val method: TgMethod = TgMethod("sendPoll")
    override var options = PollOptions()
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["question"] = question
        parameters["options"] = pollOptions
    }
}

fun poll(question: String, options: () -> Array<String>) = SendPollAction(question, options())
fun poll(question: String, vararg options: String) = SendPollAction(question, arrayOf(*options))
