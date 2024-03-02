package eu.vendeli.tgbot.types.payment

import eu.vendeli.tgbot.types.internal.Currency
import kotlinx.serialization.Serializable

/**
 * This object contains basic information about an invoice.
 * @property title Product name
 * @property description Product description
 * @property startParameter Unique bot deep-linking parameter that can be used to generate this invoice
 * @property currency Three-letter ISO 4217 currency code
 * @property totalAmount Total price in the smallest units of the currency (integer, not float/double). For example, for a price of US$ 1.45 pass amount = 145. See the exp parameter in currencies.json, it shows the number of digits past the decimal point for each currency (2 for the majority of currencies).
 * Api reference: https://core.telegram.org/bots/api#invoice
*/
@Serializable
data class Invoice(
    val title: String,
    val description: String,
    val startParameter: String,
    val currency: Currency,
    val totalAmount: Int,
)
