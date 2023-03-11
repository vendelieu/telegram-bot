@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.features.MarkupFeature
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.InvoiceOptions
import eu.vendeli.tgbot.utils.builders.InvoiceData
import eu.vendeli.tgbot.utils.getReturnType

class SendInvoiceAction(data: InvoiceData) :
    Action<Message>,
    ActionState(),
    OptionsFeature<SendInvoiceAction, InvoiceOptions>,
    MarkupFeature<SendInvoiceAction> {
    override val method: TgMethod = TgMethod("sendInvoice")
    override val returnType = getReturnType()
    override var options = InvoiceOptions()
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
