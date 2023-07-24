package eu.vendeli.tgbot.utils.builders

import eu.vendeli.tgbot.types.internal.Currency
import eu.vendeli.tgbot.types.payment.LabeledPrice

/**
 * @param title Product name, 1-32 characters
 * @param description Product description, 1-255 characters
 * @param payload Bot-defined invoice payload, 1-128 bytes. This will not be displayed to the user,
 * use for your internal processes.
 * @param providerToken Payment provider token, obtained via BotFather
 * @param currency Three-letter ISO 4217 currency code
 * @param prices Price breakdown (e.g. product price, tax, discount, delivery cost, delivery tax, bonus, etc.)
 */
data class InvoiceData(
    var title: String,
    var description: String,
    var payload: String,
    var providerToken: String,
    var currency: Currency?,
    var prices: List<LabeledPrice>,
) {
    internal companion object {
        private fun InvoiceData.blankCheck(): InvoiceData {
            require(
                title.isNotEmpty() && description.isNotEmpty() && currency != null &&
                    payload.isNotEmpty() && providerToken.isNotEmpty() && prices.isNotEmpty(),
            ) {
                "All fields must be present."
            }
            return this
        }

        fun apply(block: InvoiceData.() -> Unit): InvoiceData =
            InvoiceData("", "", "", "", null, emptyList()).apply(block).blankCheck()
    }
}
