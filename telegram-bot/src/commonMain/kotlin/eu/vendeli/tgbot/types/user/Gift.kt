package eu.vendeli.tgbot.types.user

import eu.vendeli.tgbot.types.media.Sticker
import kotlinx.serialization.Serializable

/**
 * This object represents a gift that can be sent by the bot.
 *
 * [Api reference](https://core.telegram.org/bots/api#gift)
 * @property id Unique identifier of the gift
 * @property sticker The sticker that represents the gift
 * @property starCount The number of Telegram Stars that must be paid to send the sticker
 * @property totalCount Optional. The total number of the gifts of this type that can be sent; for limited gifts only
 * @property remainingCount Optional. The number of remaining gifts of this type that can be sent; for limited gifts only
 */
@Serializable
data class Gift(
    val id: String,
    val sticker: Sticker,
    val starCount: Int,
    val totalCount: Int? = null,
    val remainingCount: Int? = null,
)
