@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.features.MarkupAble
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionAble
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.InvoiceOptions
import eu.vendeli.tgbot.utils.builders.InvoiceData

class SendInvoiceAction(data: InvoiceData) :
    Action<Message>,
    OptionAble,
    MarkupAble,
    OptionsFeature<SendInvoiceAction, InvoiceOptions>,
    MarkupFeature<SendInvoiceAction> {
    override val method: TgMethod = TgMethod("sendInvoice")
    override var options = InvoiceOptions()
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        data.checkIsAllFieldsPresent()

        parameters["title"] = data.title
        parameters["description"] = data.description
        parameters["payload"] = data.payload
        parameters["provider_token"] = data.providerToken
        parameters["currency"] = data.currency.name
        parameters["prices"] = data.prices
    }
}

fun invoice(block: InvoiceData.() -> Unit) =
    SendInvoiceAction(InvoiceData().apply(block))

fun invoice(data: InvoiceData) = SendInvoiceAction(data)
