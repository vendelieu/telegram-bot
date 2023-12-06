@file:Suppress("MatchingDeclarationName", "TooManyFunctions")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.InlineMode
import eu.vendeli.tgbot.interfaces.TgAction
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
    Action<Message>,
    ActionState(),
    InlineMode<Message>,
    EntitiesContextBuilder,
    OptionsFeature<EditMessageTextAction, EditMessageOptions>,
    MarkupFeature<EditMessageTextAction>,
    EntitiesFeature<EditMessageTextAction> {
        override val TgAction<Message>.method: TgMethod
            get() = TgMethod("editMessageText")
        override val TgAction<Message>.returnType: Class<Message>
            get() = getReturnType()
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
    Action<Message>,
    ActionState(),
    InlineMode<Message>,
    OptionsFeature<EditMessageCaptionAction, EditCaptionOptions>,
    MarkupFeature<EditMessageCaptionAction>,
    EntitiesContextBuilder,
    CaptionFeature<EditMessageCaptionAction> {
    override val TgAction<Message>.method: TgMethod
        get() = TgMethod("editMessageCaption")
    override val TgAction<Message>.returnType: Class<Message>
        get() = getReturnType()
    override val OptionsFeature<EditMessageCaptionAction, EditCaptionOptions>.options: EditCaptionOptions
        get() = EditCaptionOptions()
    override val EntitiesContextBuilder.entitiesField: String
        get() = "caption_entities"

    constructor(messageId: Long) : this() {
        parameters["message_id"] = messageId
    }
}

class EditMessageMediaAction :
    Action<Message>,
    ActionState,
    InlineMode<Message>,
    MarkupFeature<EditMessageMediaAction> {
    override val TgAction<Message>.method: TgMethod
        get() = TgMethod("editMessageMedia")
    override val TgAction<Message>.returnType: Class<Message>
        get() = getReturnType()

    constructor(inputMedia: InputMedia) {
        parameters["media"] = inputMedia
    }

    constructor(messageId: Long, inputMedia: InputMedia) {
        parameters["message_id"] = messageId
        parameters["media"] = inputMedia
    }
}

class EditMessageReplyMarkupAction() :
    Action<Message>,
    ActionState(),
    InlineMode<Message>,
    MarkupFeature<EditMessageReplyMarkupAction> {
    override val TgAction<Message>.method: TgMethod
        get() = TgMethod("editMessageReplyMarkup")
    override val TgAction<Message>.returnType: Class<Message>
        get() = getReturnType()

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
