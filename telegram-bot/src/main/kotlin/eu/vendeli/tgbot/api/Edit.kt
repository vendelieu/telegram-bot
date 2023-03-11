@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.InlineMode
import eu.vendeli.tgbot.interfaces.features.AllFeaturesPack
import eu.vendeli.tgbot.interfaces.features.CaptionFeature
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.InputMedia
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.EditCaptionOptions
import eu.vendeli.tgbot.types.internal.options.EditMessageOptions
import eu.vendeli.tgbot.utils.getReturnType

class EditMessageTextAction :
    Action<Message>,
    ActionState,
    InlineMode<Message>,
    AllFeaturesPack<EditMessageTextAction, EditMessageOptions> {
    override val method: TgMethod = TgMethod("editMessageText")
    override val returnType = getReturnType()
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
    ActionState(),
    InlineMode<Message>,
    OptionsFeature<EditMessageCaptionAction, EditCaptionOptions>,
    MarkupFeature<EditMessageCaptionAction>,
    CaptionFeature<EditMessageCaptionAction> {
    override val method: TgMethod = TgMethod("editMessageCaption")
    override val returnType = getReturnType()
    override var options = EditCaptionOptions()
    constructor(messageId: Long) : this() {
        parameters["message_id"] = messageId
    }
}

class EditMessageMediaAction : Action<Message>, ActionState, InlineMode<Message>, MarkupFeature<EditMessageMediaAction> {
    override val method: TgMethod = TgMethod("editMessageMedia")
    override val returnType = getReturnType()

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
    ActionState(),
    InlineMode<Message>,
    MarkupFeature<EditMessageMarkupAction> {
    override val method: TgMethod = TgMethod("editMessageReplyMarkup")
    override val returnType = getReturnType()

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
