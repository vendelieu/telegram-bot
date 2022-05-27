package com.github.vendelieu.tgbot.api

import com.github.vendelieu.tgbot.interfaces.Action
import com.github.vendelieu.tgbot.interfaces.features.*
import com.github.vendelieu.tgbot.types.MessageId
import com.github.vendelieu.tgbot.types.internal.TgMethod
import com.github.vendelieu.tgbot.types.internal.options.CopyMessageOptions

class CopyMessageAction :
    Action<MessageId>,
    OptionAble,
    MarkupAble,
    CaptionAble,
    OptionsFeature<CopyMessageAction, CopyMessageOptions>,
    MarkupFeature<CopyMessageAction>,
    CaptionFeature<CopyMessageAction> {
    override val method: TgMethod = TgMethod("copyMessage")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()
    override var options = CopyMessageOptions()

    constructor(fromChatId: Int, messageId: Long) {
        parameters["from_chat_id"] = fromChatId
        parameters["message_id"] = messageId
    }

    constructor(fromChatId: String, messageId: Long) {
        parameters["from_chat_id"] = fromChatId
        parameters["message_id"] = messageId
    }
}

fun copyMessage(fromChatId: Int, messageId: Long) = CopyMessageAction(fromChatId, messageId)
fun copyMessage(fromChatId: String, messageId: Long) = CopyMessageAction(fromChatId, messageId)
