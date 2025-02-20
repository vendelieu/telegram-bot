package eu.vendeli.tgbot.types.common

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.types.component.Currency
import eu.vendeli.tgbot.types.component.ParseMode
import eu.vendeli.tgbot.types.msg.MessageEntity
import eu.vendeli.tgbot.types.payment.LabeledPrice
import kotlinx.serialization.Serializable

/**
 * This object represents the content of a message to be sent as a result of an inline query. Telegram clients currently support the following 5 types:
 * - InputTextMessageContent
 * - InputLocationMessageContent
 * - InputVenueMessageContent
 * - InputContactMessageContent
 * - InputInvoiceMessageContent
 *
 * [Api reference](https://core.telegram.org/bots/api#inputmessagecontent)
 *
 */
@Serializable
sealed class InputMessageContent {
    @Serializable
    @TgAPI.Name("InputTextMessageContent")
    data class Text(
        val messageText: String,
        val parseMode: ParseMode? = null,
        val entities: List<MessageEntity>? = null,
        val linkPreviewOptions: LinkPreviewOptions? = null,
    ) : InputMessageContent()

    @Serializable
    @TgAPI.Name("InputLocationMessageContent")
    data class LocationContent(
        val latitude: Float,
        val longitude: Float,
        val horizontalAccuracy: Float? = null,
        val livePeriod: Int? = null,
        val heading: Int? = null,
        val proximityAlertRadius: Int? = null,
    ) : InputMessageContent()

    @Serializable
    @TgAPI.Name("InputVenueMessageContent")
    data class VenueContent(
        val latitude: Float,
        val longitude: Float,
        val title: String,
        val address: String,
        val foursquareId: String? = null,
        val foursquareType: String? = null,
        val googlePlaceId: String? = null,
        val googlePlaceType: String? = null,
    ) : InputMessageContent()

    @Serializable
    @TgAPI.Name("InputContactMessageContent")
    data class ContactContent(
        val phoneNumber: String,
        val firstName: String,
        val lastName: String? = null,
        val vcard: String? = null,
    ) : InputMessageContent()

    @Serializable
    @TgAPI.Name("InputInvoiceMessageContent")
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
}
