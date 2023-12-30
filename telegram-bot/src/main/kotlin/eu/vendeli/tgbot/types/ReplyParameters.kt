package eu.vendeli.tgbot.types

import eu.vendeli.tgbot.types.internal.Identifier

data class ReplyParameters(
    val messageId: Int,
    val chatId: Identifier<*>? = null,
    val allowSendingWithoutReply: Boolean? = null,
    val quote: String? = null,
    val quoteParseMode: String? = null,
    val quoteEntities: List<MessageEntity>? = null,
    val quotePosition: Int? = null,
)
