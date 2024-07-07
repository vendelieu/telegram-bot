package eu.vendeli.tgbot.types.payment

import eu.vendeli.tgbot.types.internal.Currency
import kotlinx.serialization.Serializable

@Serializable
data class RefundedPayment(
    val currency: Currency = Currency.XTR,
    val totalAmount: Int,
    val invoicePayload: String,
    val telegramPaymentChargeId: String,
    val providerPaymentChargeId: String? = null
)
