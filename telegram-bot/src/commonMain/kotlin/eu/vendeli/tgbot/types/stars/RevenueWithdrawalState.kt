package eu.vendeli.tgbot.types.stars

import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlin.time.Instant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer

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
    @OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)
    val type: String by lazy {
        this::class.serializer().descriptor.serialName
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
