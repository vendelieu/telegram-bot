package eu.vendeli.tgbot.types.payment

import eu.vendeli.tgbot.types.User
import kotlinx.serialization.Serializable

@Serializable
data class ShippingQuery(
    val id: String,
    val from: User,
    val invoicePayload: String,
    val shippingAddress: ShippingAddress,
)
