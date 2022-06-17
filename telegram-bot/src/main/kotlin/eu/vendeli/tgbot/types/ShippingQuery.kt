package eu.vendeli.tgbot.types

data class ShippingQuery(
    val id: String,
    val from: User,
    val invoicePayload: String,
    val shippingAddress: ShippingAddress
)
