package eu.vendeli.tgbot.types.internal.options

import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class ForwardMessageOptions(
    @Serializable(InstantSerializer::class)
    var videoStartTimestamp: Instant? = null,
    var disableNotification: Boolean? = null,
    var protectContent: Boolean? = null,
    var messageThreadId: Int? = null,
) : Options
