package eu.vendeli.tgbot.types.media

import kotlinx.serialization.Serializable

@Serializable
data class VideoChatEnded(
    val duration: Int,
)
