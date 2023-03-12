package eu.vendeli.tgbot.types.payment

data class ShippingOption(
    val id: String,
    val title: String,
    val prices: List<LabeledPrice>,
)
