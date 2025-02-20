@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.message

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.component.Identifier
import eu.vendeli.tgbot.types.options.ForwardMessageOptions
import eu.vendeli.tgbot.types.msg.Message
import eu.vendeli.tgbot.utils.internal.encodeWith
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.serde.DynamicLookupSerializer
import eu.vendeli.tgbot.utils.internal.toJsonElement

@TgAPI
class ForwardMessageAction(
    fromChatId: Identifier,
    messageId: Long,
) : Action<Message>(),
    OptionsFeature<ForwardMessageAction, ForwardMessageOptions> {
    @TgAPI.Name("forwardMessage")
    override val method = "forwardMessage"
    override val returnType = getReturnType()
    override val options = ForwardMessageOptions()

    init {
        parameters["from_chat_id"] = fromChatId.encodeWith(DynamicLookupSerializer)
        parameters["message_id"] = messageId.toJsonElement()
    }
}

/**
 * Use this method to forward messages of any kind. Service messages and messages with protected content can't be forwarded. On success, the sent Message is returned.
 *
 * [Api reference](https://core.telegram.org/bots/api#forwardmessage)
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
 * @param fromChatId Unique identifier for the chat where the original message was sent (or channel username in the format @channelusername)
 * @param videoStartTimestamp New start timestamp for the forwarded video in the message
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the forwarded message from forwarding and saving
 * @param messageId Message identifier in the chat specified in from_chat_id
 * @returns [Message]
 */
@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun forwardMessage(fromChatId: Identifier, messageId: Long) = ForwardMessageAction(fromChatId, messageId)

@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun forwardMessage(fromChatId: Long, messageId: Long) = forwardMessage(Identifier.from(fromChatId), messageId)

@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun forwardMessage(fromChatId: String, messageId: Long) = forwardMessage(Identifier.from(fromChatId), messageId)

@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun forwardMessage(fromChatId: User, messageId: Long) = forwardMessage(Identifier.from(fromChatId), messageId)

@Suppress("NOTHING_TO_INLINE")
@TgAPI
inline fun forwardMessage(fromChatId: Chat, messageId: Long) = forwardMessage(Identifier.from(fromChatId.id), messageId)
