package eu.vendeli.tgbot.types.common

import eu.vendeli.tgbot.types.component.Identifier
import eu.vendeli.tgbot.types.msg.MessageEntity
import kotlinx.serialization.Serializable

/**
 * Describes reply parameters for the message that is being sent.
 *
 * [Api reference](https://core.telegram.org/bots/api#replyparameters)
 * @property messageId Optional. Identifier of the message that will be replied to in the current chat, or in the chat chat_id if it is specified. Required if ephemeral_message_id isn't specified.
 * @property chatId Optional. If the message to be replied to is from a different chat, unique identifier for the chat or username of the bot, supergroup or channel in the format @username. Not supported for messages sent on behalf of a business account, messages from channel direct messages chats and ephemeral messages.
 * @property ephemeralMessageId Optional. Identifier of the incoming ephemeral message that will be replied to in the current chat. A reply to an ephemeral message must itself be an ephemeral message. An ephemeral message may only be replied to within 15 seconds of being sent. Required if message_id isn't specified.
 * @property allowSendingWithoutReply Optional. Pass True if the message should be sent even if the specified message to be replied to is not found. Always False for replies in another chat or forum topic, and sent ephemeral messages. Always True for messages sent on behalf of a business account.
 * @property quote Optional. Quoted part of the message to be replied to; 0-1024 characters after entities parsing. The quote must be an exact substring of the message to be replied to, including bold, italic, underline, strikethrough, spoiler, custom_emoji, and date_time entities. The message will fail to send if the quote isn't found in the original message. Ignored for ephemeral messages.
 * @property quoteParseMode Optional. Mode for parsing entities in the quote. See formatting options for more details.
 * @property quoteEntities Optional. A JSON-serialized list of special entities that appear in the quote. It can be specified instead of quote_parse_mode.
 * @property quotePosition Optional. Position of the quote in the original message in UTF-16 code units
 * @property checklistTaskId Optional. Identifier of the specific checklist task to be replied to
 * @property pollOptionId Optional. Persistent identifier of the specific poll option to be replied to
 */
@Serializable
data class ReplyParameters(
    var messageId: Long? = null,
    var chatId: Identifier? = null,
    var ephemeralMessageId: Long? = null,
    var allowSendingWithoutReply: Boolean? = null,
    var quote: String? = null,
    var quoteParseMode: String? = null,
    var quoteEntities: List<MessageEntity>? = null,
    var quotePosition: Int? = null,
    var checklistTaskId: Long? = null,
    var pollOptionId: String? = null,
)
