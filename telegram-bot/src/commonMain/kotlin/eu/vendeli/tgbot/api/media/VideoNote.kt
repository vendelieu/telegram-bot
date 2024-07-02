@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.interfaces.BusinessActionExt
import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.InputFile
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.VideoNoteOptions
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.handleImplicitFile
import eu.vendeli.tgbot.utils.toImplicitFile

class SendVideoNoteAction(
    videoNote: ImplicitFile,
) : MediaAction<Message>(),
    BusinessActionExt<Message>,
    OptionsFeature<SendVideoNoteAction, VideoNoteOptions>,
    MarkupFeature<SendVideoNoteAction> {
    override val method = TgMethod("sendVideoNote")
    override val returnType = getReturnType()
    override val options = VideoNoteOptions()

    init {
        handleImplicitFile(videoNote, "video_note")
    }
}

/**
 * As of v.4.0, Telegram clients support rounded square MPEG4 videos of up to 1 minute long. Use this method to send video messages. On success, the sent Message is returned.
 *
 * [Api reference](https://core.telegram.org/bots/api#sendvideonote)
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
 * @param videoNote Video note to send. Pass a file_id as String to send a video note that exists on the Telegram servers (recommended) or upload a new video using multipart/form-data. More information on Sending Files: https://core.telegram.org/bots/api#sending-files. Sending video notes by a URL is currently unsupported
 * @param duration Duration of sent video in seconds
 * @param length Video width and height, i.e. diameter of the video message
 * @param thumbnail Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can be only uploaded as a new file, so you can pass "attach://<file_attach_name>" if the thumbnail was uploaded using multipart/form-data under <file_attach_name>. More information on Sending Files: https://core.telegram.org/bots/api#sending-files
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param messageEffectId Unique identifier of the message effect to be added to the message; for private chats only
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove a reply keyboard or to force a reply from the user
 * @returns [Message]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun videoNote(file: ImplicitFile) = SendVideoNoteAction(file)
inline fun videoNote(block: () -> String) = videoNote(block().toImplicitFile())

@Suppress("NOTHING_TO_INLINE")
inline fun videoNote(ba: ByteArray) = videoNote(ba.toImplicitFile("note.mp4"))

@Suppress("NOTHING_TO_INLINE")
inline fun videoNote(input: InputFile) = videoNote(input.toImplicitFile())

@Suppress("NOTHING_TO_INLINE")
inline fun sendVideoNote(file: ImplicitFile) = videoNote(file)
inline fun sendVideoNote(block: () -> String) = videoNote(block)
