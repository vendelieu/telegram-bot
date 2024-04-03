package eu.vendeli.tgbot.types.media

import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

/**
 * This object represents a service message about a video chat scheduled in the chat.
 *
 * Api reference: https://core.telegram.org/bots/api#videochatscheduled
 * @property startDate Point in time (Unix timestamp) when the video chat is supposed to be started by a chat administrator
 */
@Serializable
data class VideoChatScheduled(
    @Serializable(InstantSerializer::class)
    val startDate: Instant,
)
