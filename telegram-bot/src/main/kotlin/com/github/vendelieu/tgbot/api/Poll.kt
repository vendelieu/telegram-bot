package com.github.vendelieu.tgbot.api

import com.github.vendelieu.tgbot.interfaces.Action
import com.github.vendelieu.tgbot.interfaces.features.MarkupAble
import com.github.vendelieu.tgbot.interfaces.features.MarkupFeature
import com.github.vendelieu.tgbot.interfaces.features.OptionAble
import com.github.vendelieu.tgbot.interfaces.features.OptionsFeature
import com.github.vendelieu.tgbot.types.Message
import com.github.vendelieu.tgbot.types.internal.TgMethod
import com.github.vendelieu.tgbot.types.internal.options.PollOptions

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
