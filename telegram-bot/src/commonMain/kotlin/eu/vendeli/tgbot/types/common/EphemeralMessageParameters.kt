package eu.vendeli.tgbot.types.common

import kotlinx.serialization.Serializable

/**

 *
 * [Api reference](https://core.telegram.org/bots/api#ephemeralmessageparameters)
 * @property receiverUserId Identifier of the user who will receive the message. It is not guaranteed that the user will receive the message, especially if they are offline. See here for more details.
 * @property callbackQueryId Optional. Identifier of the callback query which triggered the message, if any
 * @property replaceCallbackQueryMessage Optional. Pass True if the ephemeral message must be shown in place of the original message. Must be False for callback queries from ephemeral messages, which must be edited using regular editEphemeralMessage... methods.
 */
@Serializable
data class EphemeralMessageParameters(
    val receiverUserId: Long,
    val callbackQueryId: String? = null,
    val replaceCallbackQueryMessage: Boolean? = null,
)
