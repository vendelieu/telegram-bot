@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.interfaces.BusinessActionExt
import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.features.CaptionFeature
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.InputFile
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.AnimationOptions
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.handleImplicitFile
import eu.vendeli.tgbot.utils.toImplicitFile

class SendAnimationAction(animation: ImplicitFile) :
    MediaAction<Message>(),
    BusinessActionExt<Message>,
    OptionsFeature<SendAnimationAction, AnimationOptions>,
    MarkupFeature<SendAnimationAction>,
    CaptionFeature<SendAnimationAction> {
    override val method = TgMethod("sendAnimation")
    override val returnType = getReturnType()
    override val options = AnimationOptions()

    init {
        handleImplicitFile(animation, "animation")
    }
}

/**
 * Use this method to send animation files (GIF or H.264/MPEG-4 AVC video without sound). On success, the sent Message is returned. Bots can currently send animation files of up to 50 MB in size, this limit may be changed in the future.
 *
 * Api reference: https://core.telegram.org/bots/api#sendanimation
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
 * @param animation Animation to send. Pass a file_id as String to send an animation that exists on the Telegram servers (recommended), pass an HTTP URL as a String for Telegram to get an animation from the Internet, or upload a new animation using multipart/form-data. More information on Sending Files: https://core.telegram.org/bots/api#sending-files
 * @param duration Duration of sent animation in seconds
 * @param width Animation width
 * @param height Animation height
 * @param thumbnail Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can be only uploaded as a new file, so you can pass "attach://<file_attach_name>" if the thumbnail was uploaded using multipart/form-data under <file_attach_name>. More information on Sending Files: https://core.telegram.org/bots/api#sending-files
 * @param caption Animation caption (may also be used when resending animation by file_id), 0-1024 characters after entities parsing
 * @param parseMode Mode for parsing entities in the animation caption. See formatting options for more details.
 * @param captionEntities A JSON-serialized list of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param hasSpoiler Pass True if the animation needs to be covered with a spoiler animation
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove a reply keyboard or to force a reply from the user. Not supported for messages sent on behalf of a business account
 * @returns [Message]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun animation(file: ImplicitFile) = SendAnimationAction(file)
inline fun animation(block: () -> String) = animation(block().toImplicitFile())

@Suppress("NOTHING_TO_INLINE")
inline fun animation(ba: ByteArray) = animation(ba.toImplicitFile("image.gif"))

@Suppress("NOTHING_TO_INLINE")
inline fun animation(file: InputFile) = animation(file.toImplicitFile())
inline fun sendAnimation(block: () -> String) = animation(block)

@Suppress("NOTHING_TO_INLINE")
inline fun sendAnimation(file: ImplicitFile) = animation(file)
