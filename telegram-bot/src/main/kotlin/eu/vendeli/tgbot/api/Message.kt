@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.features.AllFeaturesPack
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.MessageOptions
import eu.vendeli.tgbot.utils.getReturnType

class SendMessageAction(
    data: String,
) : Action<Message>, ActionState(), AllFeaturesPack<SendMessageAction, MessageOptions> {
    override val method: TgMethod = TgMethod("sendMessage")
    override val returnType = getReturnType()
    override var options = MessageOptions()

    init {
        parameters["text"] = data
    }
}

fun message(text: String) = SendMessageAction(text)
fun message(text: () -> String) = SendMessageAction(text())
