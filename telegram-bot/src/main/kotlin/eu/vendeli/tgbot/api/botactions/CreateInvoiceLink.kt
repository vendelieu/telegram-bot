@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.interfaces.TgAction
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.CreateInvoiceLinkOptions
import eu.vendeli.tgbot.utils.builders.InvoiceData
import eu.vendeli.tgbot.utils.getReturnType

/**
 * Create invoice link -
 * use this method to create a link for an invoice. Returns the created invoice link as String on success.
 *
 */
class CreateInvoiceLinkAction(
    invoiceData: InvoiceData,
) : SimpleAction<String>, OptionsFeature<CreateInvoiceLinkAction, CreateInvoiceLinkOptions>, ActionState() {
    override val OptionsFeature<CreateInvoiceLinkAction, CreateInvoiceLinkOptions>.options: CreateInvoiceLinkOptions
        get() = CreateInvoiceLinkOptions()
    override val TgAction<String>.method: TgMethod
        get() = TgMethod("createInvoiceLink")
    override val TgAction<String>.returnType: Class<String>
        get() = getReturnType()

    init {
        parameters["title"] = invoiceData.title
        parameters["description"] = invoiceData.description
        parameters["payload"] = invoiceData.payload
        parameters["provider_token"] = invoiceData.providerToken
        parameters["currency"] = invoiceData.currency!!.name
        parameters["prices"] = invoiceData.prices
    }
}

fun createInvoiceLink(block: InvoiceData.() -> Unit) = CreateInvoiceLinkAction(InvoiceData.apply(block))
fun createInvoiceLink(data: InvoiceData) = CreateInvoiceLinkAction(data)
