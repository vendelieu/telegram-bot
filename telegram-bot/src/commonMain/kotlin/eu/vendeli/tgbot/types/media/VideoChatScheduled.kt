package eu.vendeli.tgbot.types.media

import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class VideoChatScheduled(
    @Serializable(InstantSerializer::class)
    val startDate: Instant,
)
