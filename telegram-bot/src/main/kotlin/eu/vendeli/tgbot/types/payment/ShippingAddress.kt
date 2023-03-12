package eu.vendeli.tgbot.types.payment

data class ShippingAddress(
    val countryCode: String,
    val state: String,
    val city: String,
    val streetLine1: String,
    val streetLine2: String,
    val postCode: String,
)
