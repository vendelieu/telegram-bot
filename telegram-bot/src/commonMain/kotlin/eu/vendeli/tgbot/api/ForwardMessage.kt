@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.Message
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.internal.Identifier
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.ForwardMessageOptions
import eu.vendeli.tgbot.utils.encodeWith
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.serde.DynamicLookupSerializer
import eu.vendeli.tgbot.utils.toJsonElement

class ForwardMessageAction(fromChatId: Identifier, messageId: Long) :
    Action<Message>(),
    OptionsFeature<ForwardMessageAction, ForwardMessageOptions> {
    override val method = TgMethod("forwardMessage")
    override val returnType = getReturnType()
    override val options = ForwardMessageOptions()

    init {
        parameters["from_chat_id"] = fromChatId.encodeWith(DynamicLookupSerializer)
        parameters["message_id"] = messageId.toJsonElement()
    }
}

/**
 * Use this method to forward messages of any kind. Service messages and messages with protected content can't be forwarded. On success, the sent Message is returned.
 * @param chatId Required 
 * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
 * @param fromChatId Required 
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the forwarded message from forwarding and saving
 * @param messageId Required 
 * @returns [Message]
 * Api reference: https://core.telegram.org/bots/api#forwardmessage
*/
@Suppress("NOTHING_TO_INLINE")
inline fun forwardMessage(fromChatId: Identifier, messageId: Long) = ForwardMessageAction(fromChatId, messageId)

@Suppress("NOTHING_TO_INLINE")
inline fun forwardMessage(fromChatId: Long, messageId: Long) = forwardMessage(Identifier.from(fromChatId), messageId)

@Suppress("NOTHING_TO_INLINE")
inline fun forwardMessage(fromChatId: String, messageId: Long) = forwardMessage(Identifier.from(fromChatId), messageId)

@Suppress("NOTHING_TO_INLINE")
inline fun forwardMessage(fromChatId: User, messageId: Long) = forwardMessage(Identifier.from(fromChatId), messageId)

@Suppress("NOTHING_TO_INLINE")
inline fun forwardMessage(fromChatId: Chat, messageId: Long) = forwardMessage(Identifier.from(fromChatId.id), messageId)
