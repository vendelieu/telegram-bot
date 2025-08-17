@file:Suppress("MatchingDeclarationName")

package eu.vendeli.tgbot.api.message

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.interfaces.action.Action
import eu.vendeli.tgbot.interfaces.features.OptionsFeature
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.component.Identifier
import eu.vendeli.tgbot.types.options.CopyMessagesOptions
import eu.vendeli.tgbot.types.msg.MessageId
import eu.vendeli.tgbot.utils.internal.encodeWith
import eu.vendeli.tgbot.utils.internal.getReturnType
import eu.vendeli.tgbot.utils.serde.DynamicLookupSerializer
import kotlinx.serialization.builtins.serializer

@TgAPI
class CopyMessagesAction(
    fromChatId: Identifier,
    messageIds: List<Long>,
) : Action<List<MessageId>>(),
    OptionsFeature<CopyMessagesAction, CopyMessagesOptions> {
    @TgAPI.Name("copyMessages")
    override val method = "copyMessages"
    override val returnType = getReturnType()
    override val options = CopyMessagesOptions()

    init {
        parameters["from_chat_id"] = fromChatId.encodeWith(DynamicLookupSerializer)
        parameters["message_ids"] = messageIds.encodeWith(Long.serializer())
    }
}

/**
 * Use this method to copy messages of any kind. If some of the specified messages can't be found or copied, they are skipped. Service messages, paid media messages, giveaway messages, giveaway winners messages, and invoice messages can't be copied. A quiz poll can be copied only if the value of the field correct_option_id is known to the bot. The method is analogous to the method forwardMessages, but the copied messages don't have a link to the original message. Album grouping is kept for copied messages. On success, an array of MessageId of the sent messages is returned.
 *
 * [Api reference](https://core.telegram.org/bots/api#copymessages)
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
 * @param directMessagesTopicId Identifier of the direct messages topic to which the messages will be sent; required if the messages are sent to a direct messages chat
 * @param fromChatId Unique identifier for the chat where the original messages were sent (or channel username in the format @channelusername)
 * @param messageIds A JSON-serialized list of 1-100 identifiers of messages in the chat from_chat_id to copy. The identifiers must be specified in a strictly increasing order.
 * @param disableNotification Sends the messages silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent messages from forwarding and saving
 * @param removeCaption Pass True to copy the messages without their captions
 * @returns [Array of MessageId]
 */
@TgAPI
inline fun copyMessages(fromChatId: Identifier, messageIds: List<Long>) =
    CopyMessagesAction(fromChatId, messageIds)

@TgAPI
inline fun copyMessages(fromChatId: Long, vararg messageId: Long) =
    copyMessages(Identifier.from(fromChatId), messageId.asList())

@TgAPI
inline fun copyMessages(fromChatId: String, vararg messageId: Long) =
    copyMessages(Identifier.from(fromChatId), messageId.asList())

@TgAPI
inline fun copyMessages(fromChatId: User, vararg messageId: Long) =
    copyMessages(Identifier.from(fromChatId), messageId.asList())

@TgAPI
inline fun copyMessages(fromChatId: Chat, vararg messageId: Long) =
    copyMessages(Identifier.from(fromChatId.id), messageId.asList())
