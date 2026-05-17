@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.message

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.BusinessActionExt
import eu.vendeli.tgbot.interfaces.action.MediaAction
import eu.vendeli.tgbot.interfaces.features.CaptionFeature
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.component.ImplicitFile
import eu.vendeli.tgbot.types.component.InputFile
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.types.options.LivePhotoOptions
import eu.vendeli.tgbot.utils.common.toImplicitFile
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.handleImplicitFile

@TgAPI
class SendLivePhotoAction(
    livePhoto: ImplicitFile,
    photo: ImplicitFile,
) : MediaAction<Message>(),
    BusinessActionExt<Message>,
    OptionsFeature<SendLivePhotoAction, LivePhotoOptions>,
    MarkupFeature<SendLivePhotoAction>,
    CaptionFeature<SendLivePhotoAction> {
    @TgAPI.Name("sendLivePhoto")
    override val method = "sendLivePhoto"
    override val returnType = getReturnType()
    override val options = LivePhotoOptions()

    init {
        handleImplicitFile(livePhoto, "live_photo")
        handleImplicitFile(photo, "photo")
    }
}

/**
 * Use this method to send live photos. On success, the sent Message is returned.
 *
 * [Api reference](https://core.telegram.org/bots/api#sendlivephoto)
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageThreadId Unique identifier for the target message thread (topic) of a forum; for forum supergroups and private chats of bots with forum topic mode enabled only
 * @param directMessagesTopicId Identifier of the direct messages topic to which the message will be sent; required if the message is sent to a direct messages chat
 * @param livePhoto Live photo video to send. The video must be no longer than 10 seconds and must not exceed 10 MB in size. Pass a file_id as String to send a video that exists on the Telegram servers (recommended), pass an HTTP URL as a String for Telegram to get a video from the Internet, or upload a new video using multipart/form-data. More information on Sending Files: https://core.telegram.org/bots/api#sending-files
 * @param photo The static photo to send. Pass a file_id as String to send a photo that exists on the Telegram servers (recommended) or pass "attach://<file_attach_name>" to upload a new photo using multipart/form-data under <file_attach_name> name. More information on Sending Files: https://core.telegram.org/bots/api#sending-files
 * @param caption Video caption (may also be used when resending videos by file_id), 0-1024 characters after entities parsing
 * @param parseMode Mode for parsing entities in the video caption. See formatting options for more details.
 * @param captionEntities A JSON-serialized list of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param showCaptionAboveMedia Pass True, if the caption must be shown above the message media
 * @param hasSpoiler Pass True if the video needs to be covered with a spoiler animation
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param allowPaidBroadcast Pass True to allow up to 1000 messages per second, ignoring broadcasting limits for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance
 * @param messageEffectId Unique identifier of the message effect to be added to the message; for private chats only
 * @param suggestedPostParameters A JSON-serialized object containing the parameters of the suggested post to send; for direct messages chats only.
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove a reply keyboard or to force a reply from the user
 * @returns [Message]
 */
@TgAPI
inline fun livePhoto(livePhoto: ImplicitFile, photo: ImplicitFile) = SendLivePhotoAction(livePhoto, photo)

@TgAPI
inline fun livePhoto(livePhoto: String, photo: String) =
    livePhoto(livePhoto.toImplicitFile(), photo.toImplicitFile())

@TgAPI
inline fun livePhoto(livePhoto: InputFile, photo: InputFile) =
    livePhoto(livePhoto.toImplicitFile(), photo.toImplicitFile())

@TgAPI
inline fun sendLivePhoto(livePhoto: ImplicitFile, photo: ImplicitFile) = livePhoto(livePhoto, photo)

@TgAPI
inline fun sendLivePhoto(livePhoto: String, photo: String) = livePhoto(livePhoto, photo)
