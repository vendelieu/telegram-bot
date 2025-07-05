@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.chat

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.interfaces.action.BusinessActionExt
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class PinChatMessageAction(
    messageId: Long,
    disableNotification: Boolean? = null,
) : Action<Boolean>(),
    BusinessActionExt<Boolean> {
    @TgAPI.Name("pinChatMessage")
    override val method = "pinChatMessage"
    override val returnType = getReturnType()

    init {
        parameters["message_id"] = messageId.toJsonElement()
        if (disableNotification != null) parameters["disable_notification"] = disableNotification.toJsonElement()
    }
}

/**
 * Use this method to add a message to the list of pinned messages in a chat. If the chat is not a private chat, the bot must be an administrator in the chat for this to work and must have the 'can_pin_messages' administrator right in a supergroup or 'can_edit_messages' administrator right in a channel. Returns True on success.
 *
 * [Api reference](https://core.telegram.org/bots/api#pinchatmessage)
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be pinned
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageId Identifier of a message to pin
 * @param disableNotification Pass True if it is not necessary to send a notification to all chat members about the new pinned message. Notifications are always disabled in channels and private chats.
 * @returns [Boolean]
 */
@TgAPI
inline fun pinChatMessage(messageId: Long, disableNotification: Boolean? = null) =
    PinChatMessageAction(messageId, disableNotification)
