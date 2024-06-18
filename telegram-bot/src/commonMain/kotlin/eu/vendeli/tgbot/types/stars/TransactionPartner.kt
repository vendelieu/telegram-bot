package eu.vendeli.tgbot.types.stars

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
sealed class TransactionPartner(val type: String) {
    @Serializable
    data class Fragment(val withdrawalState: RevenueWithdrawalState? = null) : TransactionPartner("fragment")

    @Serializable
    data class User(val user: eu.vendeli.tgbot.types.User) : TransactionPartner("user")

    @Serializable
    data object Other : TransactionPartner("other")
}
