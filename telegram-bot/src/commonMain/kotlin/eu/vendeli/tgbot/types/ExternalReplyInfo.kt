package eu.vendeli.tgbot.types

import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.game.Dice
import eu.vendeli.tgbot.types.game.Game
import eu.vendeli.tgbot.types.media.Animation
import eu.vendeli.tgbot.types.media.Audio
import eu.vendeli.tgbot.types.media.Document
import eu.vendeli.tgbot.types.media.PhotoSize
import eu.vendeli.tgbot.types.media.Sticker
import eu.vendeli.tgbot.types.media.Story
import eu.vendeli.tgbot.types.media.Video
import eu.vendeli.tgbot.types.media.VideoNote
import eu.vendeli.tgbot.types.media.Voice
import eu.vendeli.tgbot.types.payment.Invoice
import kotlinx.serialization.Serializable

@Serializable
data class ExternalReplyInfo(
    val origin: MessageOrigin,
    val chat: Chat? = null,
    val messageId: Long? = null,
    val linkPreviewOptions: LinkPreviewOptions? = null,
    val animation: Animation? = null,
    val audio: Audio? = null,
    val document: Document? = null,
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
