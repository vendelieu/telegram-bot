@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.features.EntitiesFeature
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.MessageOptions
import eu.vendeli.tgbot.utils.builders.EntitiesContextBuilder
import eu.vendeli.tgbot.utils.getReturnType

class SendMessageAction private constructor() :
    Action<Message>(),
    OptionsFeature<SendMessageAction, MessageOptions>,
    MarkupFeature<SendMessageAction>,
    EntitiesFeature<SendMessageAction>,
    EntitiesContextBuilder {
        override val method = TgMethod("sendMessage")
        override val returnType = getReturnType()
        override val options = MessageOptions()

        constructor(data: String) : this() {
            parameters["text"] = data
        }

        internal constructor(block: EntitiesContextBuilder.() -> String) : this() {
            parameters["text"] = block.invoke(this)
        }
    }

@Suppress("NOTHING_TO_INLINE")
inline fun message(text: String) = SendMessageAction(text)
fun message(block: EntitiesContextBuilder.() -> String) = SendMessageAction(block)

@Suppress("NOTHING_TO_INLINE")
inline fun sendMessage(text: String) = message(text)
fun sendMessage(block: EntitiesContextBuilder.() -> String) = message(block)
