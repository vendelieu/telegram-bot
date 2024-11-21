package eu.vendeli.tgbot.types.msg

import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

/**
 * Describes an inline message to be sent by a user of a Mini App.
 *
 * [Api reference](https://core.telegram.org/bots/api#preparedinlinemessage)
 * @property id Unique identifier of the prepared message
 * @property expirationDate Expiration date of the prepared message, in Unix time. Expired prepared messages can no longer be used
 */
@Serializable
data class PreparedInlineMessage(
    val id: String,
    @Serializable(InstantSerializer::class)
    val expirationDate: Instant,
)
