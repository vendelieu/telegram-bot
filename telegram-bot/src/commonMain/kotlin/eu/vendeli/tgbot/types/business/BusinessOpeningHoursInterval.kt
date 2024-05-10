package eu.vendeli.tgbot.types.business

import kotlinx.serialization.Serializable

/**
 * Describes an interval of time during which a business is open.
 *
 * [Api reference](https://core.telegram.org/bots/api#businessopeninghoursinterval)
 * @property openingMinute The minute's sequence number in a week, starting on Monday, marking the start of the time interval during which the business is open; 0 - 7 * 24 * 60
 * @property closingMinute The minute's sequence number in a week, starting on Monday, marking the end of the time interval during which the business is open; 0 - 8 * 24 * 60
 */
@Serializable
data class BusinessOpeningHoursInterval(
    val openingMinute: Int,
    val closingMinute: Int,
)
