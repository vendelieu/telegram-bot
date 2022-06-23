package eu.vendeli.tgbot.types.internal.options

/**
 * Create invoice link options
 *
 * @property maxTipAmount The maximum accepted amount for tips in the smallest units of the currency
 * (integer, not float/double). For example, for a maximum tip of US$ 1.45 pass max_tip_amount = 145.
 * See the exp parameter in currencies.json, it shows the number of digits past the decimal point for each currency
 * (2 for the majority of currencies). Defaults to 0
 * @property suggestedTipAmounts A JSON-serialized array of suggested amounts of tips in the smallest units of the
 * currency (integer, not float/double). At most 4 suggested tip amounts can be specified. The suggested tip amounts
 * must be positive, passed in a strictly increased order and must not exceed max_tip_amount.
 * @property providerData JSON-serialized data about the invoice, which will be shared with the payment provider.
 * A detailed description of required fields should be provided by the payment provider.
 * @property photoUrl URL of the product photo for the invoice. Can be a photo of the goods or a marketing image for a service.
 * @property photoSize Photo size in bytes
 * @property photoWidth Photo width
 * @property photoHeight Photo height
 * @property needName Pass True, if you require the user's full name to complete the order
 * @property needPhoneNumber Pass True, if you require the user's phone number to complete the order
 * @property needEmail Pass True, if you require the user's email address to complete the order
 * @property needShippingAddress Pass True, if you require the user's shipping address to complete the order
 * @property sendPhoneNumberToProvider Pass True, if the user's phone number should be sent to the provider
 * @property sendEmailToProvider Pass True, if the user's email address should be sent to the provider
 * @property isFlexible Pass True, if the final price depends on the shipping method
 */
data class CreateInvoiceLinkOptions(
    var maxTipAmount: Int? = null,
    var suggestedTipAmounts: List<Int>? = null,
    var providerData: String? = null,
    var photoUrl: String? = null,
    var photoSize: Long? = null,
    var photoWidth: Int? = null,
    var photoHeight: Int? = null,
    var needName: Boolean? = null,
    var needPhoneNumber: Boolean? = null,
    var needEmail: Boolean? = null,
    var needShippingAddress: Boolean? = null,
    var sendPhoneNumberToProvider: Boolean? = null,
    var sendEmailToProvider: Boolean? = null,
    var isFlexible: Boolean? = null,
) : OptionsInterface<InvoiceOptions>
