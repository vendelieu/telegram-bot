package eu.vendeli.tgbot.types.boost

import kotlinx.serialization.Serializable

@Serializable
data class UserChatBoosts(
    val boosts: List<ChatBoost>,
)
