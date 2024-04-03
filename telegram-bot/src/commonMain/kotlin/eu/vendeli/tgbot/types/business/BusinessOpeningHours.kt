package eu.vendeli.tgbot.types.business

import kotlinx.serialization.Serializable

@Serializable
data class BusinessOpeningHours(
    val timeZoneName: String,
    val openingHours: List<BusinessOpeningHoursInterval>,
)
