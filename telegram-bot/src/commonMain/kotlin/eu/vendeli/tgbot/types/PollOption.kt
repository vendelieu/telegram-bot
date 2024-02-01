package eu.vendeli.tgbot.types

import kotlinx.serialization.Serializable

@Serializable
data class PollOption(
    val text: String,
    val voterCount: Int,
)
