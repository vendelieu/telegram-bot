package eu.vendeli.tgbot.types.msg

import eu.vendeli.tgbot.interfaces.marker.MultipleResponse
import kotlinx.serialization.Serializable

/**
 * This object represents a unique message identifier.
 *
 * [Api reference](https://core.telegram.org/bots/api#messageid)
 * @property messageId Unique message identifier
 */
@Serializable
data class MessageId(
    val messageId: Long,
) : MultipleResponse
