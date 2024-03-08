package eu.vendeli.tgbot.types

import kotlinx.serialization.Serializable

/**
 * Represents a reaction added to a message along with the number of times it was added.
 * Api reference: https://core.telegram.org/bots/api#reactioncount
 * @property type Type of the reaction
 * @property totalCount Number of times the reaction was added
*/
@Serializable
data class ReactionCount(
    val type: ReactionType,
    val totalCount: Int,
)
