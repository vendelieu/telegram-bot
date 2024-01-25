package eu.vendeli.tgbot.types

import kotlinx.serialization.Serializable

@Serializable
data class ResponseParameters(
    val migrateToChatId: Long? = null,
    val retryAfter: Int? = null,
)
