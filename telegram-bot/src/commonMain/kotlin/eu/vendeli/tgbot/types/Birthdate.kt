package eu.vendeli.tgbot.types

import kotlinx.serialization.Serializable

/**

 *
 * [Api reference](https://core.telegram.org/bots/api#birthdate)
 * @property day Day of the user's birth; 1-31
 * @property month Month of the user's birth; 1-12
 * @property year Optional. Year of the user's birth
 */
@Serializable
data class Birthdate(
    val day: Int,
    val month: Int,
    val year: Int? = null,
)
