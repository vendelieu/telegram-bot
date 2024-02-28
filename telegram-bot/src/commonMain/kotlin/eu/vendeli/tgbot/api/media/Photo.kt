@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.features.CaptionFeature
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.types.internal.InputFile
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.PhotoOptions
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.handleImplicitFile
import eu.vendeli.tgbot.utils.toImplicitFile

class SendPhotoAction(photo: ImplicitFile) :
    MediaAction<Message>(),
    OptionsFeature<SendPhotoAction, PhotoOptions>,
    MarkupFeature<SendPhotoAction>,
    CaptionFeature<SendPhotoAction> {
    override val method = TgMethod("sendPhoto")
    override val returnType = getReturnType()
    override val options = PhotoOptions()

    init {
        handleImplicitFile(photo, "photo")
    }
}

/**
 * Use this method to send photos. On success, the sent Message is returned.
 * @param chatId Required 
 * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
 * @param photo Required 
 * @param caption Photo caption (may also be used when resending photos by file_id), 0-1024 characters after entities parsing
 * @param parseMode Mode for parsing entities in the photo caption. See formatting options for more details.
 * @param captionEntities A JSON-serialized list of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param hasSpoiler Pass True if the photo needs to be covered with a spoiler animation
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove reply keyboard or to force a reply from the user.
 * @returns [Message]
 * Api reference: https://core.telegram.org/bots/api#sendphoto
*/
@Suppress("NOTHING_TO_INLINE")
inline fun photo(file: ImplicitFile) = SendPhotoAction(file)
inline fun photo(block: () -> String) = photo(block().toImplicitFile())

@Suppress("NOTHING_TO_INLINE")
inline fun photo(ba: ByteArray) = photo(ba.toImplicitFile("photo.jpg"))

@Suppress("NOTHING_TO_INLINE")
inline fun photo(file: InputFile) = photo(file.toImplicitFile())

inline fun sendPhoto(block: () -> String) = photo(block)

@Suppress("NOTHING_TO_INLINE")
inline fun sendPhoto(file: ImplicitFile) = photo(file)
