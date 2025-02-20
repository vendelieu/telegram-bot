package eu.vendeli.tgbot.types.options

import eu.vendeli.tgbot.utils.serde.DurationSerializer
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class ForwardMessageOptions(
    @Serializable(DurationSerializer::class)
    var videoStartTimestamp: Duration? = null,
    var disableNotification: Boolean? = null,
    var protectContent: Boolean? = null,
    var messageThreadId: Int? = null,
) : Options
