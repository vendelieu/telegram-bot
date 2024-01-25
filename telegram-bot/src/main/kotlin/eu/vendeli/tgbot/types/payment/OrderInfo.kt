package eu.vendeli.tgbot.types.payment

import kotlinx.serialization.Serializable

@Serializable
data class OrderInfo(
    val name: String? = null,
    val phoneNumber: String? = null,
    val email: String? = null,
    val shippingAddress: ShippingAddress? = null,
)
