package eu.vendeli.tgbot.types.stars

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.media.PaidMedia
import eu.vendeli.tgbot.utils.serde.DurationSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Duration

/**
 * This object describes the source of a transaction, or its recipient for outgoing transactions. Currently, it can be one of
 * - TransactionPartnerUser
 * - TransactionPartnerAffiliateProgram
 * - TransactionPartnerFragment
 * - TransactionPartnerTelegramAds
 * - TransactionPartnerTelegramApi
 * - TransactionPartnerOther
 *
 * [Api reference](https://core.telegram.org/bots/api#transactionpartner)
 *
 */
@Serializable
sealed class TransactionPartner {
    @OptIn(ExperimentalSerializationApi::class)
    val type: String by lazy {
        serializer().descriptor.serialName
    }

    @Serializable
    @SerialName("fragment")
    data class Fragment(
        val withdrawalState: RevenueWithdrawalState? = null,
    ) : TransactionPartner()

    @Serializable
    @SerialName("user")
    @TgAPI.Name("TransactionPartnerUser")
    data class UserPartner(
        val user: User,
        val affiliate: AffiliateInfo? = null,
        val invoicePayload: String? = null,
        val paidMedia: List<PaidMedia>? = null,
        val paidMediaPayload: String? = null,
        @Serializable(DurationSerializer::class)
        val subscriptionPeriod: Duration? = null,
        val gift: String? = null,
    ) : TransactionPartner()

    @Serializable
    @SerialName("other")
    data object Other : TransactionPartner()

    @Serializable
    @SerialName("telegram_ads")
    data object TelegramAds : TransactionPartner()

    @Serializable
    @SerialName("telegram_api")
    data class TelegramApi(
        val requestCount: Int,
    ) : TransactionPartner()

    @Serializable
    @SerialName("affiliate_program")
    data class AffiliateProgram(
        val sponsorUser: User? = null,
        val commissionPerMille: Int,
    ) : TransactionPartner()
}
