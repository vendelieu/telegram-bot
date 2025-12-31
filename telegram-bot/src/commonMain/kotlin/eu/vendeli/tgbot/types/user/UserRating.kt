package eu.vendeli.tgbot.types.user

import kotlinx.serialization.Serializable

/**
 * This object describes the rating of a user based on their Telegram Star spendings.
 *
 * [Api reference](https://core.telegram.org/bots/api#userrating)
 * @property level Current level of the user, indicating their reliability when purchasing digital goods and services. A higher level suggests a more trustworthy customer; a negative level is likely reason for concern.
 * @property rating Numerical value of the user's rating; the higher the rating, the better
 * @property currentLevelRating The rating value required to get the current level
 * @property nextLevelRating Optional. The rating value required to get to the next level; omitted if the maximum level was reached
 */
@Serializable
data class UserRating(
    val level: Int,
    val rating: Int,
    val currentLevelRating: Int,
    val nextLevelRating: Int? = null,
)

