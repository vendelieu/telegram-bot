package eu.vendeli.tgbot.types

import kotlinx.serialization.Serializable

/**
 * This object contains information about one answer option in a poll.
 * @property text Option text, 1-100 characters
 * @property voterCount Number of users that voted for this option
 * Api reference: https://core.telegram.org/bots/api#polloption
*/
@Serializable
data class PollOption(
    val text: String,
    val voterCount: Int,
)
