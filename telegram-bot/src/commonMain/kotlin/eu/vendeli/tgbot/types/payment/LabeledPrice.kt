package eu.vendeli.tgbot.types.payment

import kotlinx.serialization.Serializable

@Serializable
data class LabeledPrice(
    val label: String,
    val amount: Int,
)
