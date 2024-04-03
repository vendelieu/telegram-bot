package eu.vendeli.tgbot.types.business

import kotlinx.serialization.Serializable

@Serializable
data class BusinessOpeningHoursInterval(
    val openingMinute: Int,
    val closingMinute: Int
)
