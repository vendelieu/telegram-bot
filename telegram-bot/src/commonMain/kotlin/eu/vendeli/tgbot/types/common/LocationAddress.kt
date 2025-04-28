package eu.vendeli.tgbot.types.common

import kotlinx.serialization.Serializable

/**
 * Describes the physical address of a location.
 *
 * [Api reference](https://core.telegram.org/bots/api#locationaddress)
 * @property countryCode The two-letter ISO 3166-1 alpha-2 country code of the country where the location is located
 * @property state Optional. State of the location
 * @property city Optional. City of the location
 * @property street Optional. Street address of the location
 */
@Serializable
data class LocationAddress(
    val countryCode: String,
    val state: String? = null,
    val city: String? = null,
    val street: String? = null,
)
