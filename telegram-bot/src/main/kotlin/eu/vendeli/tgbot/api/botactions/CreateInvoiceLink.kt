@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.ActionState
import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.CreateInvoiceLinkOptions
import eu.vendeli.tgbot.utils.builders.InvoiceData
import eu.vendeli.tgbot.utils.getReturnType

/**
 * Create invoice link -
 * use this method to create a link for an invoice. Returns the created invoice link as String on success.
 *
 * @param title Product name, 1-32 characters
 * @param description Product description, 1-255 characters
 * @param payload Bot-defined invoice payload, 1-128 bytes. This will not be displayed to the user,
 * use for your internal processes.
 * @param providerToken Payment provider token, obtained via BotFather
 * @param currency Three-letter ISO 4217 currency code
 * @param prices Price breakdown (e.g. product price, tax, discount, delivery cost, delivery tax, bonus, etc.)
 */
class CreateInvoiceLinkAction(
    invoiceData: InvoiceData,
) : SimpleAction<String>, OptionsFeature<CreateInvoiceLinkAction, CreateInvoiceLinkOptions>, ActionState() {
    override var options = CreateInvoiceLinkOptions()
    override val method: TgMethod = TgMethod("createInvoiceLink")
    override val returnType = getReturnType()

    init {
        invoiceData.checkIsAllFieldsPresent()

        parameters["title"] = invoiceData.title
        parameters["description"] = invoiceData.description
        parameters["payload"] = invoiceData.payload
        parameters["provider_token"] = invoiceData.providerToken
        parameters["currency"] = invoiceData.currency.name
        parameters["prices"] = invoiceData.prices
    }
}

fun createInvoiceLink(block: InvoiceData.() -> Unit) = CreateInvoiceLinkAction(InvoiceData().apply(block))
fun createInvoiceLink(data: InvoiceData) = CreateInvoiceLinkAction(data)
