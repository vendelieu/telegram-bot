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

/**
 * Use this method to send invoices. On success, the sent Message is returned.
 * @param chatId Required 
 * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
 * @param title Required 
 * @param description Required 
 * @param payload Required 
 * @param providerToken Required 
 * @param currency Required 
 * @param prices Required 
 * @param maxTipAmount The maximum accepted amount for tips in the smallest units of the currency (integer, not float/double). For example, for a maximum tip of US$ 1.45 pass max_tip_amount = 145. See the exp parameter in currencies.json, it shows the number of digits past the decimal point for each currency (2 for the majority of currencies). Defaults to 0
 * @param suggestedTipAmounts A JSON-serialized array of suggested amounts of tips in the smallest units of the currency (integer, not float/double). At most 4 suggested tip amounts can be specified. The suggested tip amounts must be positive, passed in a strictly increased order and must not exceed max_tip_amount.
 * @param startParameter Unique deep-linking parameter. If left empty, forwarded copies of the sent message will have a Pay button, allowing multiple users to pay directly from the forwarded message, using the same invoice. If non-empty, forwarded copies of the sent message will have a URL button with a deep link to the bot (instead of a Pay button), with the value used as the start parameter
 * @param providerData JSON-serialized data about the invoice, which will be shared with the payment provider. A detailed description of required fields should be provided by the payment provider.
 * @param photoUrl URL of the product photo for the invoice. Can be a photo of the goods or a marketing image for a service. People like it better when they see what they are paying for.
 * @param photoSize Photo size in bytes
 * @param photoWidth Photo width
 * @param photoHeight Photo height
 * @param needName Pass True if you require the user's full name to complete the order
 * @param needPhoneNumber Pass True if you require the user's phone number to complete the order
 * @param needEmail Pass True if you require the user's email address to complete the order
 * @param needShippingAddress Pass True if you require the user's shipping address to complete the order
 * @param sendPhoneNumberToProvider Pass True if the user's phone number should be sent to provider
 * @param sendEmailToProvider Pass True if the user's email address should be sent to provider
 * @param isFlexible Pass True if the final price depends on the shipping method
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup A JSON-serialized object for an inline keyboard. If empty, one 'Pay total price' button will be shown. If not empty, the first button must be a Pay button.
 * @returns [Message]
 * Api reference: https://core.telegram.org/bots/api#sendinvoice
*/
@Suppress("NOTHING_TO_INLINE")
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

@Suppress("NOTHING_TO_INLINE")
inline fun sendInvoice(
    title: String,
    description: String,
    payload: String,
    providerToken: String,
    currency: Currency,
    prices: List<LabeledPrice>,
) = invoice(title, description, payload, providerToken, currency, prices)
