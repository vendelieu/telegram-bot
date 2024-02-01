package eu.vendeli.tgbot.types

import eu.vendeli.tgbot.types.internal.Identifier
import kotlinx.serialization.Serializable

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
