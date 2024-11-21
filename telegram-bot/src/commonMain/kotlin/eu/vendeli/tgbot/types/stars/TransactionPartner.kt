package eu.vendeli.tgbot.types.stars

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.media.PaidMedia
import eu.vendeli.tgbot.utils.serde.DurationSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Duration

/**
 * This object describes the source of a transaction, or its recipient for outgoing transactions. Currently, it can be one of
 * - TransactionPartnerUser
 * - TransactionPartnerFragment
 * - TransactionPartnerTelegramAds
 * - TransactionPartnerTelegramApi
 * - TransactionPartnerOther
 *
 * [Api reference](https://core.telegram.org/bots/api#transactionpartner)
 *
 */
@Serializable
sealed class TransactionPartner(
    val type: String,
) {
    @Serializable
    @SerialName("fragment")
    data class Fragment(
        val withdrawalState: RevenueWithdrawalState? = null,
    ) : TransactionPartner("fragment")

    @Serializable
    @SerialName("user")
    @TgAPI.Name("TransactionPartnerUser")
    data class UserPartner(
        val user: User,
        val invoicePayload: String? = null,
        val paidMedia: List<PaidMedia>? = null,
        val paidMediaPayload: String? = null,
        @Serializable(DurationSerializer::class)
        val subscriptionPeriod: Duration? = null,
        val gift: String? = null,
    ) : TransactionPartner("user")

    @Serializable
    @SerialName("other")
    data object Other : TransactionPartner("other")

    @Serializable
    @SerialName("telegram_ads")
    data object TelegramAds : TransactionPartner("telegram_ads")

    @Serializable
    @SerialName("telegram_api")
    data class TelegramApi(
        val requestCount: Int,
    ) : TransactionPartner("telegram_api")
}
