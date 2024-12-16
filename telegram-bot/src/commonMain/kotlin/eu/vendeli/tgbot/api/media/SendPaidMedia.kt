package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.BusinessActionExt
import eu.vendeli.tgbot.interfaces.action.MediaAction
import eu.vendeli.tgbot.interfaces.features.CaptionFeature
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.internal.options.PaidMediaOptions
import eu.vendeli.tgbot.types.media.InputPaidMedia
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.utils.builders.ListingBuilder
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.handleImplicitFileGroup
import eu.vendeli.tgbot.utils.toJsonElement

@TgAPI
class SendPaidMediaAction(
    starCount: Int,
    media: List<InputPaidMedia>,
) : MediaAction<Message>(),
    BusinessActionExt<Message>,
    OptionsFeature<SendPaidMediaAction, PaidMediaOptions>,
    CaptionFeature<SendPaidMediaAction>,
    MarkupFeature<SendPaidMediaAction> {
    @TgAPI.Name("sendPaidMedia")
    override val method = "sendPaidMedia"
    override val returnType = getReturnType()
    override val options = PaidMediaOptions()

    init {
        parameters["star_count"] = starCount.toJsonElement()
        handleImplicitFileGroup(media, serializer = InputPaidMedia.serializer())
    }
}

/**
 * Use this method to send paid media. On success, the sent Message is returned.
 *
 * [Api reference](https://core.telegram.org/bots/api#sendpaidmedia)
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername). If the chat is a channel, all Telegram Star proceeds from this media will be credited to the chat's balance. Otherwise, they will be credited to the bot's balance.
 * @param starCount The number of Telegram Stars that must be paid to buy access to the media; 1-2500
 * @param media A JSON-serialized array describing the media to be sent; up to 10 items
 * @param payload Bot-defined paid media payload, 0-128 bytes. This will not be displayed to the user, use it for your internal processes.
 * @param caption Media caption, 0-1024 characters after entities parsing
 * @param parseMode Mode for parsing entities in the media caption. See formatting options for more details.
 * @param captionEntities A JSON-serialized list of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param showCaptionAboveMedia Pass True, if the caption must be shown above the message media
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param allowPaidBroadcast Pass True to allow up to 1000 messages per second, ignoring broadcasting limits for a fee of 0.1 Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove a reply keyboard or to force a reply from the user
 * @returns [Message]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun sendPaidMedia(starCount: Int, media: List<InputPaidMedia>) = SendPaidMediaAction(starCount, media)

@Suppress("NOTHING_TO_INLINE")
@TgAPI
fun sendPaidMedia(starCount: Int, media: ListingBuilder<InputPaidMedia>.() -> Unit) =
    sendPaidMedia(starCount, ListingBuilder.build(media))

@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun sendPaidMedia(starCount: Int, vararg media: InputPaidMedia) = sendPaidMedia(starCount, media.asList())
