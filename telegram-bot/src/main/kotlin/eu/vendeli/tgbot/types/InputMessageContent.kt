package eu.vendeli.tgbot.types

import eu.vendeli.tgbot.types.internal.Currency
import eu.vendeli.tgbot.types.payment.LabeledPrice

sealed class InputMessageContent
data class Text(
    val messageText: String,
    val parseMode: ParseMode? = null,
    val entities: List<MessageEntity>? = null,
    val disableWebPagePreview: Boolean? = null,
) : InputMessageContent()

data class LocationContent(
    val latitude: Float,
    val longitude: Float,
    val horizontalAccuracy: Float? = null,
    val livePeriod: Int? = null,
    val heading: Int? = null,
    val proximityAlertRadius: Int? = null,
) : InputMessageContent()

data class VenueContent(
    val latitude: Float,
    val longitude: Float,
    val title: String,
    val address: String,
    val foursquareId: String? = null,
    val fourSquareType: String? = null,
    val googlePlaceId: String? = null,
    val googlePlaceType: String? = null,
) : InputMessageContent()

data class ContactContent(
    val phoneNumber: String,
    val firstName: String,
    val lastName: String? = null,
    val userId: Long? = null,
    val vcard: String? = null,
) : InputMessageContent()

data class InvoiceContent(
    val title: String,
    val description: String,
    val payload: String,
    val providerToken: String,
    val currency: Currency,
    val prices: List<LabeledPrice>,
    val maxTipAmount: Int? = null,
    val suggestedTipAmounts: List<Int>? = null,
    val providerData: String? = null,
    val photoUrl: String? = null,
    val photoSize: Int? = null,
    val photoWidth: Int? = null,
    val photoHeight: Int? = null,
    val needName: Boolean? = null,
    val needPhoneNumber: Boolean? = null,
    val needEmail: Boolean? = null,
    val needShippingAddress: Boolean? = null,
    val sendPhoneNumberToProvider: Boolean? = null,
    val sendEmailToProvider: Boolean? = null,
    val isFlexible: Boolean? = null,
) : InputMessageContent()

data class ChosenInlineResult(
    val resultId: String,
    val from: User,
    val location: LocationContent? = null,
    val inlineMessageId: String? = null,
    val query: String,
) : InputMessageContent()
