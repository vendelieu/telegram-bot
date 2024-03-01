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
        if (inputMedia.media is ImplicitFile.InpFile) {
            val media = inputMedia.media as ImplicitFile.InpFile
            multipartData += media.file.toPartData(media.file.fileName)

            inputMedia.media = "attach://${media.file.fileName}".toImplicitFile()
        }
        parameters["media"] = inputMedia.encodeWith(DynamicLookupSerializer)
    }

    constructor(messageId: Long, inputMedia: InputMedia) {
        if (inputMedia.media is ImplicitFile.InpFile) {
            val media = inputMedia.media as ImplicitFile.InpFile
            multipartData += media.file.toPartData(media.file.fileName)

            inputMedia.media = "attach://${media.file.fileName}".toImplicitFile()
        }
        parameters["message_id"] = messageId.toJsonElement()
        parameters["media"] = inputMedia.encodeWith(DynamicLookupSerializer)
    }
}

/**
 * Use this method to edit animation, audio, document, photo, or video messages. If a message is part of a message album, then it can be edited only to an audio for audio albums, only to a document for document albums and to a photo or a video otherwise. When an inline message is edited, a new file can't be uploaded; use a previously uploaded file via its file_id or specify a URL. On success, if the edited message is not an inline message, the edited Message is returned, otherwise True is returned.
 * Api reference: https://core.telegram.org/bots/api#editmessagemedia
 * @param chatId Required if inline_message_id is not specified. Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageId Required if inline_message_id is not specified. Identifier of the message to edit
 * @param inlineMessageId Required if chat_id and message_id are not specified. Identifier of the inline message
 * @param media A JSON-serialized object for a new media content of the message
 * @param replyMarkup A JSON-serialized object for a new inline keyboard.
 * @returns [Message]|[Boolean]
*/
@Suppress("NOTHING_TO_INLINE")
inline fun editMessageMedia(messageId: Long, inputMedia: InputMedia) = editMedia(messageId, inputMedia)

@Suppress("NOTHING_TO_INLINE")
inline fun editMessageMedia(inputMedia: InputMedia) = editMedia(inputMedia)

fun editMedia(messageId: Long, inputMedia: InputMedia) = EditMessageMediaAction(messageId, inputMedia)

fun editMedia(inputMedia: InputMedia) = EditMessageMediaAction(inputMedia)
