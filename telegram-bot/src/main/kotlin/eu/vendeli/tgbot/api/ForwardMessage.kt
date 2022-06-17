package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.features.OptionAble
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.ForwardMessageOptions

class ForwardMessageAction(fromChatId: Int, messageId: Long) :
    Action<Message>,
    OptionAble,
    OptionsFeature<ForwardMessageAction, ForwardMessageOptions> {
    override val method: TgMethod = TgMethod("forwardMessage")
    override var options = ForwardMessageOptions()
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["from_chat_id"] = fromChatId
        parameters["message_id"] = messageId
    }
}

fun forwardMessage(fromChatId: Int, messageId: Long) = ForwardMessageAction(fromChatId, messageId)
