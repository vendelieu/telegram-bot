package eu.vendeli.tgbot.types.payment

import eu.vendeli.tgbot.types.internal.Currency
import kotlinx.serialization.Serializable

/**
 * This object contains basic information about a successful payment.
 *
 * [Api reference](https://core.telegram.org/bots/api#successfulpayment)
 * @property currency Three-letter ISO 4217 currency code
 * @property totalAmount Total price in the smallest units of the currency (integer, not float/double). For example, for a price of US$ 1.45 pass amount = 145. See the exp parameter in currencies.json, it shows the number of digits past the decimal point for each currency (2 for the majority of currencies).
 * @property invoicePayload Bot specified invoice payload
 * @property shippingOptionId Optional. Identifier of the shipping option chosen by the user
 * @property orderInfo Optional. Order information provided by the user
 * @property telegramPaymentChargeId Telegram payment identifier
 * @property providerPaymentChargeId Provider payment identifier
 */
@Serializable
data class SuccessfulPayment(
    val currency: Currency,
    val totalAmount: Int,
    val invoicePayload: String,
    val shippingOptionId: String? = null,
    val orderInfo: OrderInfo,
    val telegramPaymentChargeId: String,
    val providerPaymentChargeId: String,
)
