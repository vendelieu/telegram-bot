package eu.vendeli.tgbot.types.payment

import kotlinx.serialization.Serializable

/**
 * This object represents a shipping address.
 *
 * Api reference: https://core.telegram.org/bots/api#shippingaddress
 * @property countryCode Two-letter ISO 3166-1 alpha-2 country code
 * @property state State, if applicable
 * @property city City
 * @property streetLine1 First line for the address
 * @property streetLine2 Second line for the address
 * @property postCode Address post code
 */
@Serializable
data class ShippingAddress(
    val countryCode: String,
    val state: String,
    val city: String,
    val streetLine1: String,
    val streetLine2: String,
    val postCode: String,
)
