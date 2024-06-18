package eu.vendeli.tgbot.types.stars

import kotlinx.serialization.Serializable

/**
 * Contains a list of Telegram Star transactions.
 *
 * [Api reference](https://core.telegram.org/bots/api#startransactions)
 * @property transactions The list of transactions
 */
@Serializable
data class StarTransactions(
    val transactions: List<StarTransaction>,
)
