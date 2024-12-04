package eu.vendeli.tgbot.types.stars

import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

/**
 * Describes a Telegram Star transaction.
 *
 * [Api reference](https://core.telegram.org/bots/api#startransaction)
 * @property id Unique identifier of the transaction. Coincides with the identifier of the original transaction for refund transactions. Coincides with SuccessfulPayment.telegram_payment_charge_id for successful incoming payments from users.
 * @property amount Integer amount of Telegram Stars transferred by the transaction
 * @property nanostarAmount Optional. The number of 1/1000000000 shares of Telegram Stars transferred by the transaction; from 0 to 999999999
 * @property date Date the transaction was created in Unix time
 * @property source Optional. Source of an incoming transaction (e.g., a user purchasing goods or services, Fragment refunding a failed withdrawal). Only for incoming transactions
 * @property receiver Optional. Receiver of an outgoing transaction (e.g., a user for a purchase refund, Fragment for a withdrawal). Only for outgoing transactions
 */
@Serializable
data class StarTransaction(
    val id: String,
    val amount: Int,
    val nanostarAmount: Int? = null,
    @Serializable(InstantSerializer::class)
    val date: Instant,
    val source: TransactionPartner? = null,
    val receiver: TransactionPartner? = null,
)
