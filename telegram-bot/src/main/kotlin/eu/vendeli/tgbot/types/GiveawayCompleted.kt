package eu.vendeli.tgbot.types

data class GiveawayCompleted(
    val winnerCount: Int,
    val unclaimedPrizeCount: Int? = null,
    val giveawayMessage: Message? = null,
)
