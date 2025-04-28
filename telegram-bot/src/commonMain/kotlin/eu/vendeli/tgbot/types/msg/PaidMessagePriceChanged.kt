package eu.vendeli.tgbot.types.msg

import kotlinx.serialization.Serializable

/**
 * Describes a service message about a change in the price of paid messages within a chat.
 *
 * [Api reference](https://core.telegram.org/bots/api#paidmessagepricechanged)
 * @property paidMessageStarCount The new number of Telegram Stars that must be paid by non-administrator users of the supergroup chat for each sent message
 */
@Serializable
data class PaidMessagePriceChanged(
    val paidMessageStarCount: Int,
)
