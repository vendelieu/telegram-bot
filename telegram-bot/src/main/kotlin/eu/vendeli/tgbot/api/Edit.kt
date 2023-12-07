@file:Suppress("MatchingDeclarationName", "TooManyFunctions")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.InlinableAction
import eu.vendeli.tgbot.interfaces.features.CaptionFeature
import eu.vendeli.tgbot.interfaces.features.EntitiesFeature
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.EditCaptionOptions
import eu.vendeli.tgbot.types.internal.options.EditMessageOptions
import eu.vendeli.tgbot.types.media.InputMedia
import eu.vendeli.tgbot.utils.builders.EntitiesContextBuilder
import eu.vendeli.tgbot.utils.getReturnType

class EditMessageTextAction private constructor() :
    InlinableAction<Message>(),
    EntitiesContextBuilder,
    OptionsFeature<EditMessageTextAction, EditMessageOptions>,
    MarkupFeature<EditMessageTextAction>,
    EntitiesFeature<EditMessageTextAction> {
        override val method = TgMethod("editMessageText")
        override val returnType = getReturnType()
        override val OptionsFeature<EditMessageTextAction, EditMessageOptions>.options: EditMessageOptions
            get() = EditMessageOptions()

        constructor(messageId: Long, text: String) : this() {
            parameters["message_id"] = messageId
            parameters["text"] = text
        }

        constructor(text: String) : this() {
            parameters["text"] = text
        }

        internal constructor(block: EntitiesContextBuilder.() -> String) : this() {
            parameters["text"] = block(this)
        }
    }

class EditMessageCaptionAction() :
    InlinableAction<Message>(),
    OptionsFeature<EditMessageCaptionAction, EditCaptionOptions>,
    MarkupFeature<EditMessageCaptionAction>,
    EntitiesContextBuilder,
    CaptionFeature<EditMessageCaptionAction> {
    override val method = TgMethod("editMessageCaption")
    override val returnType = getReturnType()
    override val OptionsFeature<EditMessageCaptionAction, EditCaptionOptions>.options: EditCaptionOptions
        get() = EditCaptionOptions()
    override val EntitiesContextBuilder.entitiesField: String
        get() = "caption_entities"

    constructor(messageId: Long) : this() {
        parameters["message_id"] = messageId
    }
}

class EditMessageMediaAction :
    InlinableAction<Message>,
    MarkupFeature<EditMessageMediaAction> {
    override val method = TgMethod("editMessageMedia")
    override val returnType = getReturnType()

    constructor(inputMedia: InputMedia) {
        parameters["media"] = inputMedia
    }

    constructor(messageId: Long, inputMedia: InputMedia) {
        parameters["message_id"] = messageId
        parameters["media"] = inputMedia
    }
}

class EditMessageReplyMarkupAction() :
    InlinableAction<Message>(),
    MarkupFeature<EditMessageReplyMarkupAction> {
    override val method = TgMethod("editMessageReplyMarkup")
    override val returnType = getReturnType()

    constructor(messageId: Long) : this() {
        parameters["message_id"] = messageId
    }
}

fun editMessageText(messageId: Long, block: () -> String) = editText(messageId, block)
fun editMessageText(block: EntitiesContextBuilder.() -> String) = EditMessageTextAction(block)
fun editText(messageId: Long, block: () -> String) = EditMessageTextAction(messageId, text = block())
fun editText(block: EntitiesContextBuilder.() -> String) = EditMessageTextAction(block)

fun editMessageCaption(messageId: Long) = editCaption(messageId)
fun editMessageCaption() = editCaption()
fun editCaption(messageId: Long) = EditMessageCaptionAction(messageId)
fun editCaption() = EditMessageCaptionAction()

fun editMessageMedia(messageId: Long, inputMedia: InputMedia) = editMedia(messageId, inputMedia)
fun editMessageMedia(inputMedia: InputMedia) = editMedia(inputMedia)
fun editMedia(messageId: Long, inputMedia: InputMedia) = EditMessageMediaAction(messageId, inputMedia)
fun editMedia(inputMedia: InputMedia) = EditMessageMediaAction(inputMedia)

fun editMessageReplyMarkup(messageId: Long) = editMarkup(messageId)
fun editMessageReplyMarkup() = editMarkup()
fun editMarkup(messageId: Long) = EditMessageReplyMarkupAction(messageId)
fun editMarkup() = EditMessageReplyMarkupAction()
