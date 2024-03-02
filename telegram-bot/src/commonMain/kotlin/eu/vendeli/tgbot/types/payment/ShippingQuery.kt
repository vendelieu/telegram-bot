package eu.vendeli.tgbot.types.payment

import eu.vendeli.tgbot.types.User
import kotlinx.serialization.Serializable

/**
 * This object contains information about an incoming shipping query.
 * @property id Unique query identifier
 * @property from User who sent the query
 * @property invoicePayload Bot specified invoice payload
 * @property shippingAddress User specified shipping address
 * Api reference: https://core.telegram.org/bots/api#shippingquery
*/
@Serializable
data class ShippingQuery(
    val id: String,
    val from: User,
    val invoicePayload: String,
    val shippingAddress: ShippingAddress,
)
