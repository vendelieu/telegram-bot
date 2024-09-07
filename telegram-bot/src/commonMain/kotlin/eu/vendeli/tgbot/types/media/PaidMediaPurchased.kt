package eu.vendeli.tgbot.types.media

import eu.vendeli.tgbot.types.User
import kotlinx.serialization.Serializable

/**
 * This object contains information about a paid media purchase.
 *
 * [Api reference](https://core.telegram.org/bots/api#paidmediapurchased)
 * @property from User who purchased the media
 * @property paidMediaPayload Bot-specified paid media payload
 */
@Serializable
data class PaidMediaPurchased(
    val from: User,
    val paidMediaPayload: String,
)
