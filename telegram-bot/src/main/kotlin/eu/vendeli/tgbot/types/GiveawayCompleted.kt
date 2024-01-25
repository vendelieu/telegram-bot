package eu.vendeli.tgbot.types

import kotlinx.serialization.Serializable

@Serializable
data class GiveawayCompleted(
    val winnerCount: Int,
    val unclaimedPrizeCount: Int? = null,
    val giveawayMessage: Message? = null,
)
