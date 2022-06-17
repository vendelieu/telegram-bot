package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.features.MarkupAble
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionAble
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Currency
import eu.vendeli.tgbot.types.LabeledPrice
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.InvoiceOptions

class SendInvoiceAction(
    title: String,
    description: String,
    payload: String,
    providerToken: String,
    currency: Currency,
    prices: Array<out LabeledPrice>,
) : Action<Message>,
    OptionAble,
    MarkupAble,
    OptionsFeature<SendInvoiceAction, InvoiceOptions>,
    MarkupFeature<SendInvoiceAction> {
    override val method: TgMethod = TgMethod("sendInvoice")
    override var options = InvoiceOptions()
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["title"] = title
        parameters["description"] = description
        parameters["payload"] = payload
        parameters["provider_token"] = providerToken
        parameters["currency"] = currency.name
        parameters["prices"] = prices
    }
}

fun invoice(
    title: String,
    description: String,
    payload: String,
    providerToken: String,
    currency: Currency,
    vararg prices: LabeledPrice,
) = SendInvoiceAction(title, description, payload, providerToken, currency, prices)
