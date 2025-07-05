@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.BusinessActionExt
import eu.vendeli.tgbot.interfaces.action.MediaAction
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.component.ImplicitFile
import eu.vendeli.tgbot.types.component.InputFile
import eu.vendeli.tgbot.types.options.StickerOptions
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.handleImplicitFile
import eu.vendeli.tgbot.utils.common.toImplicitFile

@TgAPI
class SendStickerAction(
    sticker: ImplicitFile,
) : MediaAction<Message>(),
    BusinessActionExt<Message>,
    OptionsFeature<SendStickerAction, StickerOptions>,
    MarkupFeature<SendStickerAction> {
    @TgAPI.Name("sendSticker")
    override val method = "sendSticker"
    override val returnType = getReturnType()
    override val options = StickerOptions()

    init {
        handleImplicitFile(sticker, "sticker")
    }
}

/**
 * Use this method to send static .WEBP, animated .TGS, or video .WEBM stickers. On success, the sent Message is returned.
 *
 * [Api reference](https://core.telegram.org/bots/api#sendsticker)
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
 * @param sticker Sticker to send. Pass a file_id as String to send a file that exists on the Telegram servers (recommended), pass an HTTP URL as a String for Telegram to get a .WEBP sticker from the Internet, or upload a new .WEBP, .TGS, or .WEBM sticker using multipart/form-data. More information on Sending Files: https://core.telegram.org/bots/api#sending-files. Video and animated stickers can't be sent via an HTTP URL.
 * @param emoji Emoji associated with the sticker; only for just uploaded stickers
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param allowPaidBroadcast Pass True to allow up to 1000 messages per second, ignoring broadcasting limits for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance
 * @param messageEffectId Unique identifier of the message effect to be added to the message; for private chats only
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove a reply keyboard or to force a reply from the user
 * @returns [Message]
 */
@TgAPI
inline fun sticker(file: ImplicitFile) = SendStickerAction(file)

@TgAPI
inline fun sticker(block: () -> String) = sticker(block().toImplicitFile())

@TgAPI
inline fun sticker(ba: ByteArray) = sticker(ba.toImplicitFile("sticker.webp"))

@TgAPI
inline fun sticker(file: InputFile) = sticker(file.toImplicitFile())

@TgAPI
inline fun sendSticker(block: () -> String) = sticker(block)

@TgAPI
inline fun sendSticker(file: ImplicitFile) = sticker(file)
