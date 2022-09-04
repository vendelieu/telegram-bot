package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.InlineMode
import eu.vendeli.tgbot.interfaces.features.*
import eu.vendeli.tgbot.types.InputMedia
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.EditCaptionOptions
import eu.vendeli.tgbot.types.internal.options.EditMessageOptions

class EditMessageTextAction :
    Action<Message>,
    InlineMode<Message>,
    AllFeaturesAble,
    AllFeaturesPack<EditMessageTextAction, EditMessageOptions> {
    override val method: TgMethod = TgMethod("editMessageText")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()
    override var options = EditMessageOptions()

    constructor(messageId: Long, text: String) {
        parameters["message_id"] = messageId
        parameters["text"] = text
    }

    constructor(text: String) {
        parameters["text"] = text
    }
}

class EditMessageCaptionAction() :
    Action<Message>,
    InlineMode<Message>,
    OptionAble,
    MarkupAble,
    CaptionAble,
    OptionsFeature<EditMessageCaptionAction, EditCaptionOptions>,
    MarkupFeature<EditMessageCaptionAction>,
    CaptionFeature<EditMessageCaptionAction> {
    override val method: TgMethod = TgMethod("editMessageCaption")
    override var options = EditCaptionOptions()
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    constructor(messageId: Long) : this() {
        parameters["message_id"] = messageId
    }
}

class EditMessageMediaAction : Action<Message>, InlineMode<Message>, MarkupAble, MarkupFeature<EditMessageMediaAction> {
    override val method: TgMethod = TgMethod("editMessageMedia")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    constructor(inputMedia: InputMedia) {
        parameters["media"] = inputMedia
    }

    constructor(messageId: Long, inputMedia: InputMedia) {
        parameters["message_id"] = messageId
        parameters["media"] = inputMedia
    }
}

class EditMessageMarkupAction() :
    Action<Message>,
    InlineMode<Message>,
    MarkupAble,
    MarkupFeature<EditMessageMarkupAction> {
    override val method: TgMethod = TgMethod("editMessageReplyMarkup")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    constructor(messageId: Long) : this() {
        parameters["message_id"] = messageId
    }
}

fun editText(messageId: Long, block: () -> String) = EditMessageTextAction(messageId, text = block())
fun editText(block: () -> String) = EditMessageTextAction(text = block())

fun editCaption(messageId: Long) = EditMessageCaptionAction(messageId)
fun editCaption() = EditMessageCaptionAction()

fun editMedia(messageId: Long, inputMedia: InputMedia) = EditMessageMediaAction(messageId, inputMedia)
fun editMedia(inputMedia: InputMedia) = EditMessageMediaAction(inputMedia)

fun editMarkup(messageId: Long) = EditMessageMarkupAction(messageId)
fun editMarkup() = EditMessageMarkupAction()
