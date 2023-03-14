@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.interfaces.features.AllFeaturesPack
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.MessageOptions
import eu.vendeli.tgbot.utils.builders.EntitiesContextBuilder
import eu.vendeli.tgbot.utils.getReturnType

class SendMessageAction private constructor() :
    Action<Message>, ActionState(), AllFeaturesPack<SendMessageAction, MessageOptions>, EntitiesContextBuilder {
    override val TgAction<Message>.method: TgMethod
        get() = TgMethod("sendMessage")
    override val TgAction<Message>.returnType: Class<Message>
        get() = getReturnType()
    override val OptionsFeature<SendMessageAction, MessageOptions>.options: MessageOptions
        get() = MessageOptions()

    constructor(data: String) : this() {
        parameters["text"] = data
    }

    internal constructor(block: EntitiesContextBuilder.() -> String) : this() {
        parameters["text"] = block.invoke(this)
    }
}

fun message(text: String) = SendMessageAction(text)
fun message(block: EntitiesContextBuilder.() -> String) = SendMessageAction(block)
