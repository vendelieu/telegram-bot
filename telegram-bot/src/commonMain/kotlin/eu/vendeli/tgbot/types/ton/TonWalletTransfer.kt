package eu.vendeli.tgbot.types.ton

import kotlinx.serialization.Serializable

/**
 * Describes a service message about a transfer with a TON wallet.
 *
 * [Api reference](https://core.telegram.org/bots/api#tonwallettransfer)
 * @property transactionId Unique identifier of the transaction; may be empty if the transfer hasn't been sent yet
 * @property peerAddress Address of the transfer peer in the TON blockchain
 * @property amount The amount of transferred cryptocurrency, in the smallest units of the cryptocurrency, as a decimal number; negative for outgoing transfers
 * @property comment Optional. Comment added to the transfer; omitted if the comment is encrypted
 */
@Serializable
data class TonWalletTransfer(
    val transactionId: String,
    val peerAddress: String,
    val amount: String,
    val comment: String? = null,
)
