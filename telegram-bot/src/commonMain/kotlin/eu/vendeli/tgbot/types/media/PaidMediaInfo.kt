package eu.vendeli.tgbot.types.media

import kotlinx.serialization.Serializable

@Serializable
data class PaidMediaInfo(
    val starCount: Int,
    val paidMedia: List<PaidMedia>
)
