package eu.vendeli.tgbot.utils.builders

import eu.vendeli.tgbot.types.Currency
import eu.vendeli.tgbot.types.LabeledPrice

class InvoiceData {
    lateinit var title: String
    lateinit var description: String
    lateinit var payload: String
    lateinit var providerToken: String
    lateinit var currency: Currency
    lateinit var prices: List<LabeledPrice>

    constructor()

    constructor(
        title: String,
        description: String,
        payload: String,
        providerToken: String,
        currency: Currency,
        prices: List<LabeledPrice>
    ) {
        this.title = title
        this.description = description
        this.payload = payload
        this.providerToken = providerToken
        this.currency = currency
        this.prices = prices
    }

    internal fun checkIsAllFieldsPresent() {
        require(
            ::title.isInitialized && ::description.isInitialized &&
                    ::payload.isInitialized && ::providerToken.isInitialized &&
                    ::currency.isInitialized && ::prices.isInitialized
        ) {
            "All fields must be initialized"
        }
    }
}
