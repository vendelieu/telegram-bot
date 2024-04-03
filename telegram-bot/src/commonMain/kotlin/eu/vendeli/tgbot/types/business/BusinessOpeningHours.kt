package eu.vendeli.tgbot.types.business

import kotlinx.serialization.Serializable

/**

 *
 * Api reference: https://core.telegram.org/bots/api#businessopeninghours
 * @property timeZoneName Unique name of the time zone for which the opening hours are defined
 * @property openingHours List of time intervals describing business opening hours
 */
@Serializable
data class BusinessOpeningHours(
    val timeZoneName: String,
    val openingHours: List<BusinessOpeningHoursInterval>,
)
