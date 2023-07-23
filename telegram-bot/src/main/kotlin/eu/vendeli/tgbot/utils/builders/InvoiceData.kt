package eu.vendeli.tgbot.utils.builders

import eu.vendeli.tgbot.types.internal.Currency
import eu.vendeli.tgbot.types.payment.LabeledPrice

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
