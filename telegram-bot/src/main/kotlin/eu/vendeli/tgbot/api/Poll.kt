@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.PollOptions
import eu.vendeli.tgbot.utils.builders.ListingBuilder
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class SendPollAction(question: String, pollOptions: List<String>) :
    Action<Message>(),
    OptionsFeature<SendPollAction, PollOptions>,
    MarkupFeature<SendPollAction> {
    override val method = TgMethod("sendPoll")
    override val returnType = getReturnType()
    override val options = PollOptions()

    init {
        parameters["question"] = question.toJsonElement()
        parameters["options"] = pollOptions.toJsonElement()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun poll(question: String, options: List<String>) = SendPollAction(question, options)
fun poll(question: String, options: ListingBuilder<String>.() -> Unit) = poll(question, ListingBuilder.build(options))

@Suppress("NOTHING_TO_INLINE")
inline fun poll(question: String, vararg options: String) = poll(question, options.toList())
fun sendPoll(question: String, options: ListingBuilder<String>.() -> Unit) = poll(question, options)
