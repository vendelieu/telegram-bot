package eu.vendeli.tgbot.types

import eu.vendeli.tgbot.types.internal.Identifier
import kotlinx.serialization.Serializable

/**
 * Describes reply parameters for the message that is being sent.
 *
 * [Api reference](https://core.telegram.org/bots/api#replyparameters)
 * @property messageId Identifier of the message that will be replied to in the current chat, or in the chat chat_id if it is specified
 * @property chatId Optional. If the message to be replied to is from a different chat, unique identifier for the chat or username of the channel (in the format @channelusername). Not supported for messages sent on behalf of a business account.
 * @property allowSendingWithoutReply Optional. Pass True if the message should be sent even if the specified message to be replied to is not found. Always False for replies in another chat or forum topic. Always True for messages sent on behalf of a business account.
 * @property quote Optional. Quoted part of the message to be replied to; 0-1024 characters after entities parsing. The quote must be an exact substring of the message to be replied to, including bold, italic, underline, strikethrough, spoiler, and custom_emoji entities. The message will fail to send if the quote isn't found in the original message.
 * @property quoteParseMode Optional. Mode for parsing entities in the quote. See formatting options for more details.
 * @property quoteEntities Optional. A JSON-serialized list of special entities that appear in the quote. It can be specified instead of quote_parse_mode.
 * @property quotePosition Optional. Position of the quote in the original message in UTF-16 code units
 */
@Serializable
data class ReplyParameters(
    var messageId: Long,
    var chatId: Identifier? = null,
    var allowSendingWithoutReply: Boolean? = null,
    var quote: String? = null,
    var quoteParseMode: String? = null,
    var quoteEntities: List<MessageEntity>? = null,
    var quotePosition: Int? = null,
)
