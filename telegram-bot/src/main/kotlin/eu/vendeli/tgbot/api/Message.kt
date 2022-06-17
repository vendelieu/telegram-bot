package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.features.AllFeaturesAble
import eu.vendeli.tgbot.interfaces.features.AllFeaturesPack
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.MessageOptions

class SendMessageAction(
    data: String,
) : Action<Message>, AllFeaturesAble, AllFeaturesPack<SendMessageAction, MessageOptions> {
    override val method: TgMethod = TgMethod("sendMessage")
    override var options = MessageOptions()
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["text"] = data
    }
}

fun message(text: String) = SendMessageAction(text)
fun message(text: () -> String) = SendMessageAction(text())
