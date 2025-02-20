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
import eu.vendeli.tgbot.types.options.VoiceOptions
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.handleImplicitFile
import eu.vendeli.tgbot.utils.common.toImplicitFile

@TgAPI
class SendVoiceAction(
    voice: ImplicitFile,
) : MediaAction<Message>(),
    BusinessActionExt<Message>,
    OptionsFeature<SendVoiceAction, VoiceOptions>,
    MarkupFeature<SendVoiceAction>,
    CaptionFeature<SendVoiceAction> {
    @TgAPI.Name("sendVoice")
    override val method = "sendVoice"
    override val returnType = getReturnType()
    override val options = VoiceOptions()

    init {
        handleImplicitFile(voice, "voice")
    }
}

/**
 * Use this method to send audio files, if you want Telegram clients to display the file as a playable voice message. For this to work, your audio must be in an .OGG file encoded with OPUS, or in .MP3 format, or in .M4A format (other formats may be sent as Audio or Document). On success, the sent Message is returned. Bots can currently send voice messages of up to 50 MB in size, this limit may be changed in the future.
 *
 * [Api reference](https://core.telegram.org/bots/api#sendvoice)
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
 * @param voice Audio file to send. Pass a file_id as String to send a file that exists on the Telegram servers (recommended), pass an HTTP URL as a String for Telegram to get a file from the Internet, or upload a new one using multipart/form-data. More information on Sending Files: https://core.telegram.org/bots/api#sending-files
 * @param caption Voice message caption, 0-1024 characters after entities parsing
 * @param parseMode Mode for parsing entities in the voice message caption. See formatting options for more details.
 * @param captionEntities A JSON-serialized list of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param duration Duration of the voice message in seconds
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param allowPaidBroadcast Pass True to allow up to 1000 messages per second, ignoring broadcasting limits for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance
 * @param messageEffectId Unique identifier of the message effect to be added to the message; for private chats only
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove a reply keyboard or to force a reply from the user
 * @returns [Message]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun voice(file: ImplicitFile) = SendVoiceAction(file)

@TgAPI
inline fun voice(block: () -> String) = voice(block().toImplicitFile())

@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun voice(file: InputFile) = voice(file.toImplicitFile())

@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun voice(ba: ByteArray) = voice(ba.toImplicitFile("voice.ogg"))

@TgAPI
inline fun sendVoice(block: () -> String) = voice(block)

@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun sendVoice(file: ImplicitFile) = voice(file)
