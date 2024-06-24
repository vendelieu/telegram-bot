package eu.vendeli.tgbot.types.stars

import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

/**
 * This object describes the state of a revenue withdrawal operation. Currently, it can be one of
 * - RevenueWithdrawalStatePending
 * - RevenueWithdrawalStateSucceeded
 * - RevenueWithdrawalStateFailed
 *
 * [Api reference](https://core.telegram.org/bots/api#revenuewithdrawalstate)
 *
 */
@Serializable
sealed class RevenueWithdrawalState(val type: String) {
    @Serializable
    data object Pending : RevenueWithdrawalState("pending")

    @Serializable
    data class Succeeded(
        @Serializable(InstantSerializer::class)
        val date: Instant,
        val url: String,
    ) : RevenueWithdrawalState("succeeded")

    @Serializable
    data object Failed : RevenueWithdrawalState("failed")
}
