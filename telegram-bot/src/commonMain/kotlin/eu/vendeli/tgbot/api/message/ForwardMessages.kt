@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.message

import eu.vendeli.tgbot.interfaces.Action
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.MessageId
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.internal.Identifier
import eu.vendeli.tgbot.types.internal.TgMethod
import eu.vendeli.tgbot.types.internal.options.ForwardMessageOptions
import eu.vendeli.tgbot.utils.encodeWith
import eu.vendeli.tgbot.utils.getReturnType
import eu.vendeli.tgbot.utils.serde.DynamicLookupSerializer
import kotlinx.serialization.builtins.serializer

class ForwardMessagesAction(fromChatId: Identifier, messageIds: List<Long>) :
    Action<List<MessageId>>(),
    OptionsFeature<ForwardMessagesAction, ForwardMessageOptions> {
    override val method = TgMethod("forwardMessages")
    override val returnType = getReturnType()
    override val options = ForwardMessageOptions()

    init {
        parameters["from_chat_id"] = fromChatId.encodeWith(DynamicLookupSerializer)
        parameters["message_ids"] = messageIds.encodeWith(Long.serializer())
    }
}

/**
 * Use this method to forward multiple messages of any kind. If some of the specified messages can't be found or forwarded, they are skipped. Service messages and messages with protected content can't be forwarded. Album grouping is kept for forwarded messages. On success, an array of MessageId of the sent messages is returned.
 *
 * [Api reference](https://core.telegram.org/bots/api#forwardmessages)
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
 * @param fromChatId Unique identifier for the chat where the original messages were sent (or channel username in the format @channelusername)
 * @param messageIds A JSON-serialized list of 1-100 identifiers of messages in the chat from_chat_id to forward. The identifiers must be specified in a strictly increasing order.
 * @param disableNotification Sends the messages silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the forwarded messages from forwarding and saving
 * @returns [Array of MessageId]
 */
@Suppress("NOTHING_TO_INLINE")
inline fun forwardMessages(fromChatId: Identifier, messageIds: List<Long>) =
    ForwardMessagesAction(fromChatId, messageIds)

@Suppress("NOTHING_TO_INLINE")
inline fun forwardMessages(fromChatId: Long, vararg messageId: Long) =
    forwardMessages(Identifier.from(fromChatId), messageId.asList())

@Suppress("NOTHING_TO_INLINE")
inline fun forwardMessages(fromChatId: String, vararg messageId: Long) =
    forwardMessages(Identifier.from(fromChatId), messageId.asList())

@Suppress("NOTHING_TO_INLINE")
inline fun forwardMessages(fromChatId: User, vararg messageId: Long) =
    forwardMessages(Identifier.from(fromChatId), messageId.asList())

@Suppress("NOTHING_TO_INLINE")
inline fun forwardMessages(fromChatId: Chat, vararg messageId: Long) =
    forwardMessages(Identifier.from(fromChatId.id), messageId.asList())
