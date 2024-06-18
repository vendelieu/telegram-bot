package eu.vendeli.tgbot.types.stars

import kotlinx.serialization.Serializable

@Serializable
sealed class RevenueWithdrawalState(val type: String) {
    @Serializable
    data object Pending : RevenueWithdrawalState("pending")

    @Serializable
    data class Succeeded(val date: Int, val url: String) : RevenueWithdrawalState("succeeded")

    @Serializable
    data object Failed : RevenueWithdrawalState("failed")
}
