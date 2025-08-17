package eu.vendeli.tgbot.types.msg

import kotlinx.serialization.Serializable

/**
 * Describes a service message about a payment refund for a suggested post.
 *
 * [Api reference](https://core.telegram.org/bots/api#suggestedpostrefunded)
 * @property suggestedPostMessage Optional. Message containing the suggested post. Note that the Message object in this field will not contain the reply_to_message field even if it itself is a reply.
 * @property reason Reason for the refund. Currently, one of "post_deleted" if the post was deleted within 24 hours of being posted or removed from scheduled messages without being posted, or "payment_refunded" if the payer refunded their payment.
 */
@Serializable
data class SuggestedPostRefunded(
    val suggestedPostMessage: Message? = null,
    val reason: String,
)
