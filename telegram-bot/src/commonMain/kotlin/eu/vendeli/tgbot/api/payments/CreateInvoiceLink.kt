@file:Suppress("MatchingDeclarationName", "LongParameterList")

package eu.vendeli.tgbot.api.payments

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.BusinessActionExt
import eu.vendeli.tgbot.interfaces.action.SimpleAction
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.component.Currency
import eu.vendeli.tgbot.types.options.CreateInvoiceLinkOptions
import eu.vendeli.tgbot.types.payment.LabeledPrice
import eu.vendeli.tgbot.utils.internal.encodeWith
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class CreateInvoiceLinkAction(
    title: String,
    description: String,
    payload: String,
    currency: Currency,
    prices: List<LabeledPrice>,
) : SimpleAction<String>(),
    BusinessActionExt<String>,
    OptionsFeature<CreateInvoiceLinkAction, CreateInvoiceLinkOptions> {
    override val options = CreateInvoiceLinkOptions()

    @TgAPI.Name("createInvoiceLink")
    override val method = "createInvoiceLink"
    override val returnType = getReturnType()

    init {
        parameters["title"] = title.toJsonElement()
        parameters["description"] = description.toJsonElement()
        parameters["payload"] = payload.toJsonElement()
        parameters["currency"] = currency.name.toJsonElement()
        parameters["prices"] = prices.encodeWith(LabeledPrice.serializer())
    }
}

/**
 * Use this method to create a link for an invoice. Returns the created invoice link as String on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#createinvoicelink)
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the link will be created. For payments in Telegram Stars only.
 * @param title Product name, 1-32 characters
 * @param description Product description, 1-255 characters
 * @param payload Bot-defined invoice payload, 1-128 bytes. This will not be displayed to the user, use it for your internal processes.
 * @param providerToken Payment provider token, obtained via @BotFather. Pass an empty string for payments in Telegram Stars.
 * @param currency Three-letter ISO 4217 currency code, see more on currencies. Pass "XTR" for payments in Telegram Stars.
 * @param prices Price breakdown, a JSON-serialized list of components (e.g. product price, tax, discount, delivery cost, delivery tax, bonus, etc.). Must contain exactly one item for payments in Telegram Stars.
 * @param subscriptionPeriod The number of seconds the subscription will be active for before the next payment. The currency must be set to "XTR" (Telegram Stars) if the parameter is used. Currently, it must always be 2592000 (30 days) if specified. Any number of subscriptions can be active for a given bot at the same time, including multiple concurrent subscriptions from the same user. Subscription price must no exceed 2500 Telegram Stars.
 * @param maxTipAmount The maximum accepted amount for tips in the smallest units of the currency (integer, not float/double). For example, for a maximum tip of US$ 1.45 pass max_tip_amount = 145. See the exp parameter in currencies.json, it shows the number of digits past the decimal point for each currency (2 for the majority of currencies). Defaults to 0. Not supported for payments in Telegram Stars.
 * @param suggestedTipAmounts A JSON-serialized array of suggested amounts of tips in the smallest units of the currency (integer, not float/double). At most 4 suggested tip amounts can be specified. The suggested tip amounts must be positive, passed in a strictly increased order and must not exceed max_tip_amount.
 * @param providerData JSON-serialized data about the invoice, which will be shared with the payment provider. A detailed description of required fields should be provided by the payment provider.
 * @param photoUrl URL of the product photo for the invoice. Can be a photo of the goods or a marketing image for a service.
 * @param photoSize Photo size in bytes
 * @param photoWidth Photo width
 * @param photoHeight Photo height
 * @param needName Pass True if you require the user's full name to complete the order. Ignored for payments in Telegram Stars.
 * @param needPhoneNumber Pass True if you require the user's phone number to complete the order. Ignored for payments in Telegram Stars.
 * @param needEmail Pass True if you require the user's email address to complete the order. Ignored for payments in Telegram Stars.
 * @param needShippingAddress Pass True if you require the user's shipping address to complete the order. Ignored for payments in Telegram Stars.
 * @param sendPhoneNumberToProvider Pass True if the user's phone number should be sent to the provider. Ignored for payments in Telegram Stars.
 * @param sendEmailToProvider Pass True if the user's email address should be sent to the provider. Ignored for payments in Telegram Stars.
 * @param isFlexible Pass True if the final price depends on the shipping method. Ignored for payments in Telegram Stars.
 * @returns [String]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun createInvoiceLink(
    title: String,
    description: String,
    payload: String,
    currency: Currency,
    prices: List<LabeledPrice>,
) = CreateInvoiceLinkAction(title, description, payload, currency, prices)

@TgAPI
inline fun createInvoiceLink(
    title: String,
    description: String,
    currency: Currency,
    vararg prices: LabeledPrice,
    payload: () -> String,
) = createInvoiceLink(title, description, payload(), currency, prices.asList())
