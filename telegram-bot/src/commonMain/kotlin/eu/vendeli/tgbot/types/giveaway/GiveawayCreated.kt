package eu.vendeli.tgbot.types.giveaway

import kotlinx.serialization.Serializable

@Serializable
data class GiveawayCreated(
    val prizeStarCount: Int? = null,
)
