package eu.vendeli.tgbot.types

import eu.vendeli.tgbot.types.chat.Chat

data class Giveaway(
    val chats: List<Chat>,
    val winnersSelectionDate: Long,
    val winnerCount: Int,
    val onlyNewMembers: Boolean? = null,
    val hasPublicWinners: Boolean? = null,
    val prizeDescription: String? = null,
    val countryCodes: List<String>? = null,
    val premiumSubscriptionMonthCount: Int? = null,
)
