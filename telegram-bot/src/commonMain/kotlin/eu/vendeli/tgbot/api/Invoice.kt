@file:Suppress("MatchingDeclarationName", "LongParameterList")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.Currency
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.InvoiceOptions
import eu.vendeli.tgbot.types.payment.LabeledPrice
import eu.vendeli.tgbot.utils.encodeWith
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.toJsonElement

class SendInvoiceAction(
    title: String,
    description: String,
    payload: String,
    providerToken: String,
    currency: Currency,
    prices: List<LabeledPrice>,
) : Action<Message>(),
    OptionsFeature<SendInvoiceAction, InvoiceOptions>,
    MarkupFeature<SendInvoiceAction> {
    override val method = TgMethod("sendInvoice")
    override val returnType = getReturnType()
    override val options = InvoiceOptions()

    init {
        parameters["title"] = title.toJsonElement()
        parameters["description"] = description.toJsonElement()
        parameters["payload"] = payload.toJsonElement()
        parameters["provider_token"] = providerToken.toJsonElement()
        parameters["currency"] = currency.name.toJsonElement()
        parameters["prices"] = prices.encodeWith(LabeledPrice.serializer())
    }
}

inline fun invoice(
    title: String,
    description: String,
    payload: String,
    providerToken: String,
    currency: Currency,
    prices: List<LabeledPrice>,
) = SendInvoiceAction(title, description, payload, providerToken, currency, prices)
inline fun invoice(
    title: String,
    description: String,
    providerToken: String,
    currency: Currency,
    vararg prices: LabeledPrice,
    payload: () -> String,
) = invoice(title, description, payload(), providerToken, currency, prices.asList())

inline fun sendInvoice(
    title: String,
    description: String,
    payload: String,
    providerToken: String,
    currency: Currency,
    prices: List<LabeledPrice>,
) = invoice(title, description, payload, providerToken, currency, prices)
