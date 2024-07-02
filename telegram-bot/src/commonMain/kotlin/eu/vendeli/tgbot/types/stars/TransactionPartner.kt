package eu.vendeli.tgbot.types.stars

import eu.vendeli.tgbot.types.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object describes the source of a transaction, or its recipient for outgoing transactions. Currently, it can be one of
 * - TransactionPartnerFragment
 * - TransactionPartnerUser
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
    data class UserPartner(
        val user: User,
        val invoicePayload: String? = null,
    ) : TransactionPartner("user")

    @Serializable
    @SerialName("other")
    data object Other : TransactionPartner("other")

    @Serializable
    @SerialName("telegram_ads")
    data object TelegramAds : TransactionPartner("telegram_ads")
}
