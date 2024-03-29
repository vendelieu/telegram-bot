package eu.vendeli.tgbot.types

import kotlinx.serialization.Serializable

/**
 * This object contains information about one answer option in a poll.
 * Api reference: https://core.telegram.org/bots/api#polloption
 * @property text Option text, 1-100 characters
 * @property voterCount Number of users that voted for this option
*/
@Serializable
data class PollOption(
    val text: String,
    val voterCount: Int,
)
