package eu.vendeli.tgbot.types.payment

import kotlinx.serialization.Serializable

/**
 * This object represents information about an order.
 *
 * Api reference: https://core.telegram.org/bots/api#orderinfo
 * @property name Optional. User name
 * @property phoneNumber Optional. User's phone number
 * @property email Optional. User email
 * @property shippingAddress Optional. User shipping address
 */
@Serializable
data class OrderInfo(
    val name: String? = null,
    val phoneNumber: String? = null,
    val email: String? = null,
    val shippingAddress: ShippingAddress? = null,
)
