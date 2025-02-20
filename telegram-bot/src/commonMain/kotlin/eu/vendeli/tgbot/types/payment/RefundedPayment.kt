package eu.vendeli.tgbot.types.payment

import eu.vendeli.tgbot.types.component.Currency
import kotlinx.serialization.Serializable

/**
 * This object contains basic information about a refunded payment.
 *
 * [Api reference](https://core.telegram.org/bots/api#refundedpayment)
 * @property currency Three-letter ISO 4217 currency code, or "XTR" for payments in Telegram Stars. Currently, always "XTR"
 * @property totalAmount Total refunded price in the smallest units of the currency (integer, not float/double). For example, for a price of US$ 1.45, total_amount = 145. See the exp parameter in currencies.json, it shows the number of digits past the decimal point for each currency (2 for the majority of currencies).
 * @property invoicePayload Bot-specified invoice payload
 * @property telegramPaymentChargeId Telegram payment identifier
 * @property providerPaymentChargeId Optional. Provider payment identifier
 */
@Serializable
data class RefundedPayment(
    val currency: Currency = Currency.XTR,
    val totalAmount: Int,
    val invoicePayload: String,
    val telegramPaymentChargeId: String,
    val providerPaymentChargeId: String? = null,
)
