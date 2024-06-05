package eu.vendeli.tgbot.types

import eu.vendeli.tgbot.types.internal.Currency
import eu.vendeli.tgbot.types.payment.LabeledPrice
import kotlinx.serialization.Serializable

@Serializable
sealed class InputMessageContent

@Serializable
data class Text(
    val messageText: String,
    val parseMode: ParseMode? = null,
    val entities: List<MessageEntity>? = null,
    val linkPreviewOptions: LinkPreviewOptions? = null,
) : InputMessageContent()

@Serializable
data class LocationContent(
    val latitude: Float,
    val longitude: Float,
    val horizontalAccuracy: Float? = null,
    val livePeriod: Int? = null,
    val heading: Int? = null,
    val proximityAlertRadius: Int? = null,
) : InputMessageContent()

@Serializable
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

@Serializable
data class ContactContent(
    val phoneNumber: String,
    val firstName: String,
    val lastName: String? = null,
    val userId: Long? = null,
    val vcard: String? = null,
) : InputMessageContent()

@Serializable
data class InvoiceContent(
    val title: String,
    val description: String,
    val payload: String,
    val providerToken: String? = null,
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
