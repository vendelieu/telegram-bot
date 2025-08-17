@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.BusinessActionExt
import eu.vendeli.tgbot.interfaces.action.MediaAction
import eu.vendeli.tgbot.interfaces.features.CaptionFeature
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.component.ImplicitFile
import eu.vendeli.tgbot.types.component.InputFile
import eu.vendeli.tgbot.types.options.PhotoOptions
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.handleImplicitFile
import eu.vendeli.tgbot.utils.common.toImplicitFile

@TgAPI
class SendPhotoAction(
    photo: ImplicitFile,
) : MediaAction<Message>(),
    BusinessActionExt<Message>,
    OptionsFeature<SendPhotoAction, PhotoOptions>,
    MarkupFeature<SendPhotoAction>,
    CaptionFeature<SendPhotoAction> {
    @TgAPI.Name("sendPhoto")
    override val method = "sendPhoto"
    override val returnType = getReturnType()
    override val options = PhotoOptions()

    init {
        handleImplicitFile(photo, "photo")
    }
}

/**
 * Use this method to send photos. On success, the sent Message is returned.
 *
 * [Api reference](https://core.telegram.org/bots/api#sendphoto)
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
 * @param directMessagesTopicId Identifier of the direct messages topic to which the message will be sent; required if the message is sent to a direct messages chat
 * @param photo Photo to send. Pass a file_id as String to send a photo that exists on the Telegram servers (recommended), pass an HTTP URL as a String for Telegram to get a photo from the Internet, or upload a new photo using multipart/form-data. The photo must be at most 10 MB in size. The photo's width and height must not exceed 10000 in total. Width and height ratio must be at most 20. More information on Sending Files: https://core.telegram.org/bots/api#sending-files
 * @param caption Photo caption (may also be used when resending photos by file_id), 0-1024 characters after entities parsing
 * @param parseMode Mode for parsing entities in the photo caption. See formatting options for more details.
 * @param captionEntities A JSON-serialized list of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param showCaptionAboveMedia Pass True, if the caption must be shown above the message media
 * @param hasSpoiler Pass True if the photo needs to be covered with a spoiler animation
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param allowPaidBroadcast Pass True to allow up to 1000 messages per second, ignoring broadcasting limits for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance
 * @param messageEffectId Unique identifier of the message effect to be added to the message; for private chats only
 * @param suggestedPostParameters A JSON-serialized object containing the parameters of the suggested post to send; for direct messages chats only. If the message is sent as a reply to another suggested post, then that suggested post is automatically declined.
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove a reply keyboard or to force a reply from the user
 * @returns [Message]
 */
@TgAPI
inline fun photo(file: ImplicitFile) = SendPhotoAction(file)

@TgAPI
inline fun photo(block: () -> String) = photo(block().toImplicitFile())

@TgAPI
inline fun photo(ba: ByteArray) = photo(ba.toImplicitFile("photo.jpg"))

@TgAPI
inline fun photo(file: InputFile) = photo(file.toImplicitFile())

@TgAPI
inline fun sendPhoto(block: () -> String) = photo(block)

@TgAPI
inline fun sendPhoto(file: ImplicitFile) = photo(file)
