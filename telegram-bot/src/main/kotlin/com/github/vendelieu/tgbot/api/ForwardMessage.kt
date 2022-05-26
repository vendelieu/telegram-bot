package com.github.vendelieu.tgbot.api

import com.github.vendelieu.tgbot.interfaces.Action
import com.github.vendelieu.tgbot.interfaces.features.OptionAble
import com.github.vendelieu.tgbot.interfaces.features.OptionsFeature
import com.github.vendelieu.tgbot.types.Message
import com.github.vendelieu.tgbot.types.internal.TgMethod
import com.github.vendelieu.tgbot.types.internal.options.ForwardMessageOptions

class ForwardMessageAction(fromChatId: Int, messageId: Long) :
    Action<Message>,
    OptionAble,
    OptionsFeature<ForwardMessageAction, ForwardMessageOptions> {
    override val method: TgMethod = TgMethod("forwardMessage")
    override var options = ForwardMessageOptions()
    override val parameters: MutableMap<String, Any> = mutableMapOf()

    init {
        parameters["from_chat_id"] = fromChatId
        parameters["message_id"] = messageId
    }
}

fun forwardMessage(fromChatId: Int, messageId: Long) = ForwardMessageAction(fromChatId, messageId)
