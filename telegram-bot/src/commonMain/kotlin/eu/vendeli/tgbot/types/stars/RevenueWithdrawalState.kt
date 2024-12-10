package eu.vendeli.tgbot.types.stars

import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
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
sealed class RevenueWithdrawalState {
    @OptIn(ExperimentalSerializationApi::class)
    val type: String by lazy {
        serializer().descriptor.serialName
    }

    @Serializable
    @SerialName("pending")
    data object Pending : RevenueWithdrawalState()

    @Serializable
    @SerialName("succeeded")
    data class Succeeded(
        @Serializable(InstantSerializer::class)
        val date: Instant,
        val url: String,
    ) : RevenueWithdrawalState()

    @Serializable
    @SerialName("failed")
    data object Failed : RevenueWithdrawalState()
}
