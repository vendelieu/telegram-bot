package eu.vendeli.tgbot.types.media

import kotlinx.serialization.Serializable

/**
 * This object represents a service message about a video chat ended in the chat.
 *
 * Api reference: https://core.telegram.org/bots/api#videochatended
 * @property duration Video chat duration in seconds
 */
@Serializable
data class VideoChatEnded(
    val duration: Int,
)
