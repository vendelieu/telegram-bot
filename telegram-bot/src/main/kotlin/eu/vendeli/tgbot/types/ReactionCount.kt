package eu.vendeli.tgbot.types

import kotlinx.serialization.Serializable

@Serializable
data class ReactionCount(
    val type: ReactionType,
    val totalCount: Int,
)
