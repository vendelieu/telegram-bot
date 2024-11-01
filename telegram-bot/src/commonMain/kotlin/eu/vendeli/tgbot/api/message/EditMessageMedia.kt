package eu.vendeli.tgbot.api.message

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.interfaces.action.BusinessActionExt
import eu.vendeli.tgbot.interfaces.action.InlineActionExt
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.types.media.InputMedia
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.utils.encodeWith
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.serde.DynamicLookupSerializer
import eu.vendeli.tgbot.utils.toJsonElement
import eu.vendeli.tgbot.utils.transform

@TgAPI
class EditMessageMediaAction :
    Action<Message>,
    InlineActionExt<Message>,
    BusinessActionExt<Message>,
    MarkupFeature<EditMessageMediaAction> {
    @TgAPI.Name("editMessageMedia")
    override val method = "editMessageMedia"
    override val returnType = getReturnType()

    constructor(media: InputMedia) {
        media.media = media.media.transform(multipartData)
        parameters["media"] = media.encodeWith(DynamicLookupSerializer)
    }

    constructor(messageId: Long, media: InputMedia) {
        media.media = media.media.transform(multipartData)
        parameters["message_id"] = messageId.toJsonElement()
        parameters["media"] = media.encodeWith(DynamicLookupSerializer)
    }
}

/**
 * Use this method to edit animation, audio, document, photo, or video messages, or to add media to text messages. If a message is part of a message album, then it can be edited only to an audio for audio albums, only to a document for document albums and to a photo or a video otherwise. When an inline message is edited, a new file can't be uploaded; use a previously uploaded file via its file_id or specify a URL. On success, if the edited message is not an inline message, the edited Message is returned, otherwise True is returned. Note that business messages that were not sent by the bot and do not contain an inline keyboard can only be edited within 48 hours from the time they were sent.
 *
 * [Api reference](https://core.telegram.org/bots/api#editmessagemedia)
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message to be edited was sent
 * @param chatId Required if inline_message_id is not specified. Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageId Required if inline_message_id is not specified. Identifier of the message to edit
 * @param inlineMessageId Required if chat_id and message_id are not specified. Identifier of the inline message
 * @param media A JSON-serialized object for a new media content of the message
 * @param replyMarkup A JSON-serialized object for a new inline keyboard.
 * @returns [Message]|[Boolean]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun editMessageMedia(messageId: Long, inputMedia: InputMedia) = editMedia(messageId, inputMedia)

@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun editMessageMedia(inputMedia: InputMedia) = editMedia(inputMedia)

@TgAPI
fun editMedia(messageId: Long, inputMedia: InputMedia) = EditMessageMediaAction(messageId, inputMedia)

@TgAPI
fun editMedia(inputMedia: InputMedia) = EditMessageMediaAction(inputMedia)
