package eu.vendeli.tgbot.types

import eu.vendeli.tgbot.types.chat.Chat

data class GiveawayWinners(
    val chat: Chat,
    val giveawayMessageId: Long,
    val winnersSelectionDate: Long,
    val winnerCount: Int,
    val winners: List<User>,
    val additionalChatCount: Int? = null,
    val premiumSubscriptionMonthCount: Int? = null,
    val unclaimedPrizeCount: Int? = null,
    val onlyNewMembers: Boolean? = null,
    val wasRefunded: Boolean? = null,
    val prizeDescription: String? = null,
)
