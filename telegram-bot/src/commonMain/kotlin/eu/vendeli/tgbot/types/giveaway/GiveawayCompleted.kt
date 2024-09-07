package eu.vendeli.tgbot.types.giveaway

import eu.vendeli.tgbot.types.msg.Message
import kotlinx.serialization.Serializable

/**
 * This object represents a service message about the completion of a giveaway without public winners.
 *
 * [Api reference](https://core.telegram.org/bots/api#giveawaycompleted)
 * @property winnerCount Number of winners in the giveaway
 * @property unclaimedPrizeCount Optional. Number of undistributed prizes
 * @property giveawayMessage Optional. Message with the giveaway that was completed, if it wasn't deleted
 */
@Serializable
data class GiveawayCompleted(
    val winnerCount: Int,
    val unclaimedPrizeCount: Int? = null,
    val giveawayMessage: Message? = null,
    val isStarGiveaway: Boolean? = null,
)
