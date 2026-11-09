package eu.vendeli.tgbot.types.common

import kotlinx.serialization.Serializable

/**

 *
 * [Api reference](https://core.telegram.org/bots/api#ephemeralmessageparameters)
 * @property receiverUserId Optional. Identifier of the user who will receive the message; required unless the message is sent as an ephemeral reply to a callback query. If omitted, then the parameter chat_id of the method is ignored. If specified together with callback_query_id, then it must be the identifier of the user who pressed the button. Must be omitted for messages sent in chats where the bot isn't a member, unless the message is a reply to an ephemeral message. It is not guaranteed that the user will receive the message, especially if they are offline. See here for more details.
 * @property callbackQueryId Optional. Identifier of the callback query which triggered the message, if any. If specified and receiver_user_id is omitted, then the message is sent as an ephemeral reply to the callback query and will be shown only to the user who pressed the button. Ephemeral replies can't be sent to callback queries from messages in channel direct messages chats. The bot must still answer the callback query using answerCallbackQuery.
 * @property replaceCallbackQueryMessage Optional. Pass True if the ephemeral message must be shown in place of the original message. Must be False for callback queries from ephemeral messages, which must be edited using regular editEphemeralMessage... methods.
 */
@Serializable
data class EphemeralMessageParameters(
    val receiverUserId: Long,
    val callbackQueryId: String? = null,
    val replaceCallbackQueryMessage: Boolean? = null,
)
