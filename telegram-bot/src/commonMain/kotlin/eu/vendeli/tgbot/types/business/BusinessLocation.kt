package eu.vendeli.tgbot.types.business

import eu.vendeli.tgbot.types.common.Location
import kotlinx.serialization.Serializable

/**
 * Contains information about the location of a Telegram Business account.
 *
 * [Api reference](https://core.telegram.org/bots/api#businesslocation)
 * @property address Address of the business
 * @property location Optional. Location of the business
 */
@Serializable
data class BusinessLocation(
    val address: String,
    val location: Location? = null,
)
