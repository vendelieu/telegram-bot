package eu.vendeli.tgbot.types.msg

import eu.vendeli.tgbot.types.gift.StarAmount
import kotlinx.serialization.Serializable

/**
 * Describes a service message about a successful payment for a suggested post.
 *
 * [Api reference](https://core.telegram.org/bots/api#suggestedpostpaid)
 * @property suggestedPostMessage Optional. Message containing the suggested post. Note that the Message object in this field will not contain the reply_to_message field even if it itself is a reply.
 * @property currency Currency in which the payment was made. Currently, one of "XTR" for Telegram Stars or "TON" for toncoins
 * @property amount Optional. The amount of the currency that was received by the channel in nanotoncoins; for payments in toncoins only
 * @property starAmount Optional. The amount of Telegram Stars that was received by the channel; for payments in Telegram Stars only
 */
@Serializable
data class SuggestedPostPaid(
    val suggestedPostMessage: Message? = null,
    val currency: String,
    val amount: Long? = null,
    val starAmount: StarAmount? = null,
)
