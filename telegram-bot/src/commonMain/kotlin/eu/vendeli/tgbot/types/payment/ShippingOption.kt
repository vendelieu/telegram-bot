package eu.vendeli.tgbot.types.payment

import kotlinx.serialization.Serializable

@Serializable
data class ShippingOption(
    val id: String,
    val title: String,
    val prices: List<LabeledPrice>,
)
