package eu.vendeli.tgbot.types.gift

import kotlinx.serialization.Serializable

/**
 * Describes an amount of Telegram Stars.
 *
 * [Api reference](https://core.telegram.org/bots/api#staramount)
 * @property amount Integer amount of Telegram Stars, rounded to 0; can be negative
 * @property nanostarAmount Optional. The number of 1/1000000000 shares of Telegram Stars; from -999999999 to 999999999; can be negative if and only if amount is non-positive
 */
@Serializable
data class StarAmount(
    val amount: Int,
    val nanostarAmount: Int? = null,
)
