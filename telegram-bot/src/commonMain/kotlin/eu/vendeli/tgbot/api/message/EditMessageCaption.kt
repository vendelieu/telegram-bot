package eu.vendeli.tgbot.api.message

import eu.vendeli.tgbot.interfaces.InlinableAction
import eu.vendeli.tgbot.interfaces.features.CaptionFeature
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.EditCaptionOptions
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class EditMessageCaptionAction() :
    InlinableAction<Message>(),
    OptionsFeature<EditMessageCaptionAction, EditCaptionOptions>,
    MarkupFeature<EditMessageCaptionAction>,
    CaptionFeature<EditMessageCaptionAction> {
    override val method = TgMethod("editMessageCaption")
    override val returnType = getReturnType()
    override val options = EditCaptionOptions()

    constructor(messageId: Long) : this() {
        parameters["message_id"] = messageId.toJsonElement()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun editMessageCaption(messageId: Long) = editCaption(messageId)

@Suppress("NOTHING_TO_INLINE")
inline fun editMessageCaption() = editCaption()

@Suppress("NOTHING_TO_INLINE")
inline fun editCaption(messageId: Long) = EditMessageCaptionAction(messageId)

@Suppress("NOTHING_TO_INLINE")
inline fun editCaption() = EditMessageCaptionAction()
