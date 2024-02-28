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
import eu.vendeli.tgbot.types.internal.options.VoiceOptions
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.handleImplicitFile
import eu.vendeli.tgbot.utils.toImplicitFile

class SendVoiceAction(voice: ImplicitFile) :
    MediaAction<Message>(),
    OptionsFeature<SendVoiceAction, VoiceOptions>,
    MarkupFeature<SendVoiceAction>,
    CaptionFeature<SendVoiceAction> {
    override val method = TgMethod("sendVoice")
    override val returnType = getReturnType()
    override val options = VoiceOptions()

    init {
        handleImplicitFile(voice, "voice")
    }
}

/**
 * Use this method to send audio files, if you want Telegram clients to display the file as a playable voice message. For this to work, your audio must be in an .OGG file encoded with OPUS (other formats may be sent as Audio or Document). On success, the sent Message is returned. Bots can currently send voice messages of up to 50 MB in size, this limit may be changed in the future.
 * @param chatId Required 
 * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
 * @param voice Required 
 * @param caption Voice message caption, 0-1024 characters after entities parsing
 * @param parseMode Mode for parsing entities in the voice message caption. See formatting options for more details.
 * @param captionEntities A JSON-serialized list of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param duration Duration of the voice message in seconds
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove reply keyboard or to force a reply from the user.
 * @returns [Message]
 * Api reference: https://core.telegram.org/bots/api#sendvoice
*/
@Suppress("NOTHING_TO_INLINE")
inline fun voice(file: ImplicitFile) = SendVoiceAction(file)
inline fun voice(block: () -> String) = voice(block().toImplicitFile())

@Suppress("NOTHING_TO_INLINE")
inline fun voice(file: InputFile) = voice(file.toImplicitFile())

@Suppress("NOTHING_TO_INLINE")
inline fun voice(ba: ByteArray) = voice(ba.toImplicitFile("voice.ogg"))

inline fun sendVoice(block: () -> String) = voice(block)

@Suppress("NOTHING_TO_INLINE")
inline fun sendVoice(file: ImplicitFile) = voice(file)
