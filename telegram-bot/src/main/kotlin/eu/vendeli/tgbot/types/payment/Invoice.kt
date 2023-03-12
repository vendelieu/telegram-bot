package eu.vendeli.tgbot.types.payment

import eu.vendeli.tgbot.types.internal.Currency

data class Invoice(
    val title: String,
    val description: String,
    val startParameter: String,
    val currency: Currency,
    val totalAmount: Int,
)
