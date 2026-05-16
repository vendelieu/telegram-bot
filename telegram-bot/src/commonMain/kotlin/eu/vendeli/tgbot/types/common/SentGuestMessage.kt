package eu.vendeli.tgbot.types.common

import eu.vendeli.tgbot.interfaces.marker.MultipleResponse
import kotlinx.serialization.Serializable

/**
 * Describes an inline message sent by a guest bot.
 *
 * [Api reference](https://core.telegram.org/bots/api#sentguestmessage)
 * @property inlineMessageId Identifier of the sent inline message
 */
@Serializable
data class SentGuestMessage(
    val inlineMessageId: String,
) : MultipleResponse
