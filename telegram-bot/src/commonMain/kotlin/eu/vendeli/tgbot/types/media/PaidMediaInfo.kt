package eu.vendeli.tgbot.types.media

import kotlinx.serialization.Serializable

/**
 * Describes the paid media added to a message.
 *
 * [Api reference](https://core.telegram.org/bots/api#paidmediainfo)
 * @property starCount The number of Telegram Stars that must be paid to buy access to the media
 * @property paidMedia Information about the paid media
 */
@Serializable
data class PaidMediaInfo(
    val starCount: Int,
    val paidMedia: List<PaidMedia>,
)
