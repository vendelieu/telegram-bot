@file:Suppress("MatchingDeclarationName")
package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.interfaces.features.OptionAble
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Currency
import eu.vendeli.tgbot.types.LabeledPrice
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.CreateInvoiceLinkOptions

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
    title: String,
    description: String,
    payload: String,
    providerToken: String,
    currency: Currency,
    prices: List<LabeledPrice>,
) : SimpleAction<String>, OptionAble, OptionsFeature<CreateInvoiceLinkAction, CreateInvoiceLinkOptions> {
    override var options = CreateInvoiceLinkOptions()
    override val method: TgMethod = TgMethod("createInvoiceLink")
    override val parameters: MutableMap<String, Any?> = mutableMapOf()

    init {
        parameters["title"] = title
        parameters["description"] = description
        parameters["payload"] = payload
        parameters["provider_token"] = providerToken
        parameters["currency"] = currency
        parameters["prices"] = prices
    }
}

fun createInvoiceLink(
    title: String,
    description: String,
    payload: String,
    providerToken: String,
    currency: Currency,
    prices: List<LabeledPrice>,
) = CreateInvoiceLinkAction(title, description, payload, providerToken, currency, prices)
