package eu.vendeli.tgbot.types.payment

import eu.vendeli.tgbot.types.User

data class ShippingQuery(
    val id: String,
    val from: User,
    val invoicePayload: String,
    val shippingAddress: ShippingAddress,
)
