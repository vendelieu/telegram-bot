package eu.vendeli.tgbot.types.business

import eu.vendeli.tgbot.types.Location
import kotlinx.serialization.Serializable

/**

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
