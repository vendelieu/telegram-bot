package com.github.vendelieu.tgbot.api

import com.github.vendelieu.tgbot.interfaces.Action
import com.github.vendelieu.tgbot.interfaces.features.AllFeaturesAble
import com.github.vendelieu.tgbot.interfaces.features.AllFeaturesPack
import com.github.vendelieu.tgbot.types.Message
import com.github.vendelieu.tgbot.types.internal.TgMethod
import com.github.vendelieu.tgbot.types.internal.options.MessageOptions

class SendMessageAction(
    data: String,
) : Action<Message>, AllFeaturesAble, AllFeaturesPack<SendMessageAction, MessageOptions> {
    override val method: TgMethod = TgMethod("sendMessage")
    override var options = MessageOptions()
    override val parameters: MutableMap<String, Any> = mutableMapOf()

    init {
        parameters["text"] = data
    }
}

fun message(text: String) = SendMessageAction(text)
fun message(text: () -> String) = SendMessageAction(text())
