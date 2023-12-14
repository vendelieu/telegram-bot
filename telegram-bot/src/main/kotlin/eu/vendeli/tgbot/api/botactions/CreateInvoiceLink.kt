@file:Suppress("MatchingDeclarationName", "LongParameterList")

package eu.vendeli.tgbot.api.botactions

import eu.vendeli.tgbot.interfaces.SimpleAction
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.internal.Currency
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.CreateInvoiceLinkOptions
import eu.vendeli.tgbot.types.payment.LabeledPrice
import eu.vendeli.tgbot.utils.getReturnType

/**
 * Create invoice link -
 * use this method to create a link for an invoice. Returns the created invoice link as String on success.
 *
 */
class CreateInvoiceLinkAction(
    title: String,
    description: String,
    payload: String,
    providerToken: String,
    currency: Currency,
    prices: List<LabeledPrice>,
) : SimpleAction<String>(), OptionsFeature<CreateInvoiceLinkAction, CreateInvoiceLinkOptions> {
    override val options = CreateInvoiceLinkOptions()
    override val method = TgMethod("createInvoiceLink")
    override val returnType = getReturnType()

    init {
        parameters["title"] = title
        parameters["description"] = description
        parameters["payload"] = payload
        parameters["provider_token"] = providerToken
        parameters["currency"] = currency.name
        parameters["prices"] = prices
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun createInvoiceLink(
    title: String,
    description: String,
    payload: String,
    providerToken: String,
    currency: Currency,
    prices: List<LabeledPrice>,
) = CreateInvoiceLinkAction(title, description, payload, providerToken, currency, prices)

inline fun createInvoiceLink(
    title: String,
    description: String,
    providerToken: String,
    currency: Currency,
    vararg prices: LabeledPrice,
    payload: () -> String,
) = createInvoiceLink(title, description, payload(), providerToken, currency, prices.asList())
