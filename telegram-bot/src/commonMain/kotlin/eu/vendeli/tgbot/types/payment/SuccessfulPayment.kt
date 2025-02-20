package eu.vendeli.tgbot.types.payment

import eu.vendeli.tgbot.types.component.Currency
import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

/**
 * This object contains basic information about a successful payment. Note that if the buyer initiates a chargeback with the relevant payment provider following this transaction, the funds may be debited from your balance. This is outside of Telegram's control.
 *
 * [Api reference](https://core.telegram.org/bots/api#successfulpayment)
 * @property currency Three-letter ISO 4217 currency code, or "XTR" for payments in Telegram Stars
 * @property totalAmount Total price in the smallest units of the currency (integer, not float/double). For example, for a price of US$ 1.45 pass amount = 145. See the exp parameter in currencies.json, it shows the number of digits past the decimal point for each currency (2 for the majority of currencies).
 * @property invoicePayload Bot-specified invoice payload
 * @property subscriptionExpirationDate Optional. Expiration date of the subscription, in Unix time; for recurring payments only
 * @property isRecurring Optional. True, if the payment is a recurring payment for a subscription
 * @property isFirstRecurring Optional. True, if the payment is the first payment for a subscription
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
    val orderInfo: OrderInfo? = null,
    @Serializable(InstantSerializer::class)
    val subscriptionExpirationDate: Instant? = null,
    val isRecurring: Boolean? = null,
    val isFirstRecurring: Boolean? = null,
    val telegramPaymentChargeId: String,
    val providerPaymentChargeId: String,
)
