package eu.vendeli.tgbot.types.boost

import kotlinx.serialization.Serializable

@Serializable
data class ChatBoostAdded(
    val boostCount: Int
)
