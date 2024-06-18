package eu.vendeli.tgbot.types.stars

import kotlinx.serialization.Serializable

@Serializable
sealed class TransactionPartner(val type: String) {
    @Serializable
    data class Fragment(val withdrawalState: RevenueWithdrawalState? = null) : TransactionPartner("fragment")

    @Serializable
    data class User(val user: eu.vendeli.tgbot.types.User) : TransactionPartner("user")

    @Serializable
    data object Other : TransactionPartner("other")
}
