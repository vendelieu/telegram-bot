@file:Suppress("MatchingDeclarationName", "TooManyFunctions")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.InlinableAction
import eu.vendeli.tgbot.interfaces.features.CaptionFeature
import eu.vendeli.tgbot.interfaces.features.EntitiesFeature
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.EditCaptionOptions
import eu.vendeli.tgbot.types.internal.options.EditMessageOptions
import eu.vendeli.tgbot.types.media.InputMedia
import eu.vendeli.tgbot.utils.builders.EntitiesContextBuilder
import eu.vendeli.tgbot.utils.encodeWith
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.serde.DynamicLookupSerializer
import eu.vendeli.tgbot.utils.toImplicitFile
import eu.vendeli.tgbot.utils.toJsonElement
import eu.vendeli.tgbot.utils.toPartData

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

class EditMessageMediaAction :
    InlinableAction<Message>,
    MarkupFeature<EditMessageMediaAction> {
    override val method = TgMethod("editMessageMedia")
    override val returnType = getReturnType()

    constructor(inputMedia: InputMedia) {
        handleMedia(inputMedia)
        parameters["media"] = inputMedia.encodeWith(DynamicLookupSerializer)
    }

    constructor(messageId: Long, inputMedia: InputMedia) {
        handleMedia(inputMedia)
        parameters["message_id"] = messageId.toJsonElement()
        parameters["media"] = inputMedia.encodeWith(DynamicLookupSerializer)
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun handleMedia(inputMedia: InputMedia) {
        if (inputMedia.media is ImplicitFile.InpFile) {
            val media = inputMedia.media as ImplicitFile.InpFile
            multipartData += media.file.toPartData(media.file.fileName)

            inputMedia.media = "attach://${media.file.fileName}".toImplicitFile()
        }
    }
}

class EditMessageReplyMarkupAction() :
    InlinableAction<Message>(),
    MarkupFeature<EditMessageReplyMarkupAction> {
    override val method = TgMethod("editMessageReplyMarkup")
    override val returnType = getReturnType()

    constructor(messageId: Long) : this() {
        parameters["message_id"] = messageId.toJsonElement()
    }
}

fun editMessageText(block: EntitiesContextBuilder<EditMessageTextAction>.() -> String) = EditMessageTextAction(block)
inline fun editText(messageId: Long, block: () -> String) = EditMessageTextAction(messageId, text = block())
fun editText(block: EntitiesContextBuilder<EditMessageTextAction>.() -> String) = EditMessageTextAction(block)

@Suppress("NOTHING_TO_INLINE")
inline fun editCaption(messageId: Long) = EditMessageCaptionAction(messageId)

@Suppress("NOTHING_TO_INLINE")
inline fun editCaption() = EditMessageCaptionAction()

@Suppress("NOTHING_TO_INLINE")
inline fun editMedia(messageId: Long, inputMedia: InputMedia) = EditMessageMediaAction(messageId, inputMedia)

@Suppress("NOTHING_TO_INLINE")
inline fun editMedia(inputMedia: InputMedia) = EditMessageMediaAction(inputMedia)

@Suppress("NOTHING_TO_INLINE")
inline fun editMarkup(messageId: Long) = EditMessageReplyMarkupAction(messageId)

@Suppress("NOTHING_TO_INLINE")
inline fun editMarkup() = EditMessageReplyMarkupAction()

inline fun editMessageText(messageId: Long, block: () -> String) = editText(messageId, block)

@Suppress("NOTHING_TO_INLINE")
inline fun editMessageCaption(messageId: Long) = editCaption(messageId)

@Suppress("NOTHING_TO_INLINE")
inline fun editMessageCaption() = editCaption()

@Suppress("NOTHING_TO_INLINE")
inline fun editMessageMedia(messageId: Long, inputMedia: InputMedia) = editMedia(messageId, inputMedia)

@Suppress("NOTHING_TO_INLINE")
inline fun editMessageMedia(inputMedia: InputMedia) = editMedia(inputMedia)

@Suppress("NOTHING_TO_INLINE")
inline fun editMessageReplyMarkup(messageId: Long) = editMarkup(messageId)

@Suppress("NOTHING_TO_INLINE")
inline fun editMessageReplyMarkup() = editMarkup()
