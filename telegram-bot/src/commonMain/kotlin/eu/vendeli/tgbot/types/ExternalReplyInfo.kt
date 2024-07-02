package eu.vendeli.tgbot.types

import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.game.Dice
import eu.vendeli.tgbot.types.game.Game
import eu.vendeli.tgbot.types.media.Animation
import eu.vendeli.tgbot.types.media.Audio
import eu.vendeli.tgbot.types.media.Document
import eu.vendeli.tgbot.types.media.PaidMediaInfo
import eu.vendeli.tgbot.types.media.PhotoSize
import eu.vendeli.tgbot.types.media.Sticker
import eu.vendeli.tgbot.types.media.Story
import eu.vendeli.tgbot.types.media.Video
import eu.vendeli.tgbot.types.media.VideoNote
import eu.vendeli.tgbot.types.media.Voice
import eu.vendeli.tgbot.types.payment.Invoice
import eu.vendeli.tgbot.types.poll.Poll
import kotlinx.serialization.Serializable

/**
 * This object contains information about a message that is being replied to, which may come from another chat or forum topic.
 *
 * [Api reference](https://core.telegram.org/bots/api#externalreplyinfo)
 * @property origin Origin of the message replied to by the given message
 * @property chat Optional. Chat the original message belongs to. Available only if the chat is a supergroup or a channel.
 * @property messageId Optional. Unique message identifier inside the original chat. Available only if the original chat is a supergroup or a channel.
 * @property linkPreviewOptions Optional. Options used for link preview generation for the original message, if it is a text message
 * @property animation Optional. Message is an animation, information about the animation
 * @property audio Optional. Message is an audio file, information about the file
 * @property document Optional. Message is a general file, information about the file
 * @property photo Optional. Message is a photo, available sizes of the photo
 * @property sticker Optional. Message is a sticker, information about the sticker
 * @property story Optional. Message is a forwarded story
 * @property video Optional. Message is a video, information about the video
 * @property videoNote Optional. Message is a video note, information about the video message
 * @property voice Optional. Message is a voice message, information about the file
 * @property hasMediaSpoiler Optional. True, if the message media is covered by a spoiler animation
 * @property contact Optional. Message is a shared contact, information about the contact
 * @property dice Optional. Message is a dice with random value
 * @property game Optional. Message is a game, information about the game. More about games: https://core.telegram.org/bots/api#games
 * @property giveaway Optional. Message is a scheduled giveaway, information about the giveaway
 * @property giveawayWinners Optional. A giveaway with public winners was completed
 * @property invoice Optional. Message is an invoice for a payment, information about the invoice. More about payments: https://core.telegram.org/bots/api#payments
 * @property location Optional. Message is a shared location, information about the location
 * @property poll Optional. Message is a native poll, information about the poll
 * @property venue Optional. Message is a venue, information about the venue
 */
@Serializable
data class ExternalReplyInfo(
    val origin: MessageOrigin,
    val chat: Chat? = null,
    val messageId: Long? = null,
    val linkPreviewOptions: LinkPreviewOptions? = null,
    val animation: Animation? = null,
    val audio: Audio? = null,
    val document: Document? = null,
    val paidMedia: PaidMediaInfo? = null,
    val photo: List<PhotoSize>? = null,
    val sticker: Sticker? = null,
    val story: Story? = null,
    val video: Video? = null,
    val videoNote: VideoNote? = null,
    val voice: Voice? = null,
    val hasMediaSpoiler: Boolean? = null,
    val contact: Contact? = null,
    val dice: Dice? = null,
    val game: Game? = null,
    val giveaway: Giveaway? = null,
    val giveawayWinners: GiveawayWinners? = null,
    val invoice: Invoice? = null,
    val location: Location? = null,
    val poll: Poll? = null,
    val venue: Venue? = null,
)
