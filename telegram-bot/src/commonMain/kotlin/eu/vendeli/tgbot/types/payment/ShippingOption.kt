package eu.vendeli.tgbot.types.payment

import kotlinx.serialization.Serializable

/**
 * This object represents one shipping option.
 *
 * [Api reference](https://core.telegram.org/bots/api#shippingoption)
 * @property id Shipping option identifier
 * @property title Option title
 * @property prices List of price portions
 */
@Serializable
data class ShippingOption(
    val id: String,
    val title: String,
    val prices: List<LabeledPrice>,
)
