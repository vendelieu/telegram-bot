package eu.vendeli.tgbot.types.stars

import kotlinx.serialization.Serializable

@Serializable
data class StarTransactions(
    val transactions: List<StarTransaction>
)
