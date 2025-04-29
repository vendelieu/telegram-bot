package eu.vendeli.tgbot.types.stars

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TransactionType {
    @SerialName("invoice_payment")
    INVOICE_PAYMENT,

    @SerialName("paid_media_payment")
    PAID_MEDIA_PAYMENT,

    @SerialName("gift_purchase")
    GIFT_PURCHASE,

    @SerialName("premium_purchase")
    PREMIUM_PURCHASE,

    @SerialName("business_account_transfer")
    BUSINESS_ACCOUNT_TRANSFER,
}
