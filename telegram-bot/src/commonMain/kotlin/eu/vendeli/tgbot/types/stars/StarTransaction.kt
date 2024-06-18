package eu.vendeli.tgbot.types.stars

import kotlinx.serialization.Serializable

@Serializable
data class StarTransaction(
    val id: String,
    val amount: Int,
    val date: Int,
    val source: TransactionPartner? = null,
    val receiver: TransactionPartner? = null,
)
