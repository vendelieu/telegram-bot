package eu.vendeli.tgbot.api.message

import eu.vendeli.tgbot.interfaces.InlinableAction
import eu.vendeli.tgbot.interfaces.features.EntitiesFeature
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.EditMessageOptions
import eu.vendeli.tgbot.utils.builders.EntitiesContextBuilder
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class EditMessageTextAction private constructor() :
    InlinableAction<Message>(),
    EntitiesContextBuilder<EditMessageTextAction>,
    OptionsFeature<EditMessageTextAction, EditMessageOptions>,
    MarkupFeature<EditMessageTextAction>,
    EntitiesFeature<EditMessageTextAction> {
    override val method = TgMethod("editMessageText")
    override val returnType = getReturnType()
    override val options = EditMessageOptions()

    constructor(messageId: Long, text: String) : this() {
        parameters["message_id"] = messageId.toJsonElement()
        parameters["text"] = text.toJsonElement()
    }

    constructor(text: String) : this() {
        parameters["text"] = text.toJsonElement()
    }

    internal constructor(block: EntitiesContextBuilder<EditMessageTextAction>.() -> String) : this() {
        parameters["text"] = block(this).toJsonElement()
    }
}

inline fun editMessageText(messageId: Long, block: () -> String) = editText(messageId, block)

fun editMessageText(block: EntitiesContextBuilder<EditMessageTextAction>.() -> String) = EditMessageTextAction(block)
inline fun editText(messageId: Long, block: () -> String) = EditMessageTextAction(messageId, text = block())
fun editText(block: EntitiesContextBuilder<EditMessageTextAction>.() -> String) = EditMessageTextAction(block)
