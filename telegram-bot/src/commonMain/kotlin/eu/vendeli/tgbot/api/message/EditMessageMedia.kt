package eu.vendeli.tgbot.api.message

import eu.vendeli.tgbot.interfaces.InlinableAction
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.media.InputMedia
import eu.vendeli.tgbot.utils.encodeWith
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.serde.DynamicLookupSerializer
import eu.vendeli.tgbot.utils.toImplicitFile
import eu.vendeli.tgbot.utils.toJsonElement
import eu.vendeli.tgbot.utils.toPartData

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

@Suppress("NOTHING_TO_INLINE")
inline fun editMessageMedia(messageId: Long, inputMedia: InputMedia) = editMedia(messageId, inputMedia)

@Suppress("NOTHING_TO_INLINE")
inline fun editMessageMedia(inputMedia: InputMedia) = editMedia(inputMedia)

@Suppress("NOTHING_TO_INLINE")
inline fun editMedia(messageId: Long, inputMedia: InputMedia) = EditMessageMediaAction(messageId, inputMedia)

@Suppress("NOTHING_TO_INLINE")
inline fun editMedia(inputMedia: InputMedia) = EditMessageMediaAction(inputMedia)
