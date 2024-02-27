package eu.vendeli.tgbot.types

import eu.vendeli.tgbot.types.boost.ChatBoost
import kotlinx.serialization.Serializable

@Serializable
data class UserChatBoosts(
    val boosts: List<ChatBoost>,
)
