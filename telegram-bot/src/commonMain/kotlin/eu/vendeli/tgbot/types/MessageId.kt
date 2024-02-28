package eu.vendeli.tgbot.types

import eu.vendeli.tgbot.interfaces.MultipleResponse
import kotlinx.serialization.Serializable

/**
 * This object represents a unique message identifier.
 * @property messageId Unique message identifier
 * Api reference: https://core.telegram.org/bots/api#messageid
*/
@Serializable
data class MessageId(val messageId: Long) : MultipleResponse
