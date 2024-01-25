package eu.vendeli.tgbot.types

import kotlinx.serialization.Serializable

@Serializable
data class UserChatBoosts(
    val boosts: List<ChatBoost>,
)
