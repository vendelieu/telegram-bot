package eu.vendeli.tgbot.types

import kotlinx.serialization.Serializable

@Serializable
data class UsersShared(
    val requestId: Int,
    val userId: List<Long>,
)
