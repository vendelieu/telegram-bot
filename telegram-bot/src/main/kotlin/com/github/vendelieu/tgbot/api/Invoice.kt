package com.github.vendelieu.tgbot.api

import com.github.vendelieu.tgbot.interfaces.Action
import com.github.vendelieu.tgbot.interfaces.features.MarkupAble
import com.github.vendelieu.tgbot.interfaces.features.MarkupFeature
import com.github.vendelieu.tgbot.interfaces.features.OptionAble
import com.github.vendelieu.tgbot.interfaces.features.OptionsFeature
import com.github.vendelieu.tgbot.types.Currency
import com.github.vendelieu.tgbot.types.LabeledPrice
import com.github.vendelieu.tgbot.types.Message
import com.github.vendelieu.tgbot.types.internal.TgMethod
import com.github.vendelieu.tgbot.types.internal.options.InvoiceOptions

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
