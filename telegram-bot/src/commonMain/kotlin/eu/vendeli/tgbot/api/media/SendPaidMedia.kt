package eu.vendeli.tgbot.api.media

import eu.vendeli.tgbot.interfaces.MediaAction
import eu.vendeli.tgbot.interfaces.features.CaptionFeature
import eu.vendeli.tgbot.interfaces.features.EntitiesFeature
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.PaidMediaOptions
import eu.vendeli.tgbot.types.media.InputPaidMedia
import eu.vendeli.tgbot.utils.builders.ListingBuilder
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.handleImplicitFileGroup
import eu.vendeli.tgbot.utils.toJsonElement

class SendPaidMediaAction(
    starCount: Int,
    inputPaidMedia: List<InputPaidMedia>,
) : MediaAction<Message>(),
    OptionsFeature<SendPaidMediaAction, PaidMediaOptions>,
    CaptionFeature<SendPaidMediaAction>,
    EntitiesFeature<SendPaidMediaAction>,
    MarkupFeature<SendPaidMediaAction> {
    override val method = TgMethod("sendPaidMedia")
    override val returnType = getReturnType()
    override val options = PaidMediaOptions()

    init {
        parameters["star_count"] = starCount.toJsonElement()
        handleImplicitFileGroup(inputPaidMedia)
    }
}

/**
 * Use this method to send paid media to channel chats. On success, the sent Message is returned.
 *
 * [Api reference](https://core.telegram.org/bots/api#sendpaidmedia)
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param starCount The number of Telegram Stars that must be paid to buy access to the media
 * @param media A JSON-serialized array describing the media to be sent; up to 10 items
 * @param caption Media caption, 0-1024 characters after entities parsing
 * @param parseMode Mode for parsing entities in the media caption. See formatting options for more details.
 * @param captionEntities A JSON-serialized list of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param showCaptionAboveMedia Pass True, if the caption must be shown above the message media
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove a reply keyboard or to force a reply from the user
 * @returns [Message]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun sendPaidMedia(starCount: Int, media: List<InputPaidMedia>) = SendPaidMediaAction(starCount, media)

@Suppress("NOTHING_TO_INLINE")
fun sendPaidMedia(starCount: Int, media: ListingBuilder<InputPaidMedia>.() -> Unit) =
    sendPaidMedia(starCount, ListingBuilder.build(media))

@Suppress("NOTHING_TO_INLINE")
inline fun sendPaidMedia(starCount: Int, vararg media: InputPaidMedia) = sendPaidMedia(starCount, media.asList())
