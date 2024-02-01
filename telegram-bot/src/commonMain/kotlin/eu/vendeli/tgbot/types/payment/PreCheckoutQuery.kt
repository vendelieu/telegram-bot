package eu.vendeli.tgbot.types.payment

import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.Currency
import kotlinx.serialization.Serializable

@Serializable
data class PreCheckoutQuery(
    val id: String,
    val from: User,
    val currency: Currency,
    val totalAmount: Int,
    val invoicePayload: String,
    val shippingOptionId: String? = null,
    val orderInfo: OrderInfo? = null,
)
