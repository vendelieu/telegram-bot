package eu.vendeli.tgbot.types

import kotlinx.serialization.Serializable

/**
 * This object contains information about the quoted part of a message that is replied to by the given message.
 *
 * [Api reference](https://core.telegram.org/bots/api#textquote)
 * @property text Text of the quoted part of a message that is replied to by the given message
 * @property entities Optional. Special entities that appear in the quote. Currently, only bold, italic, underline, strikethrough, spoiler, and custom_emoji entities are kept in quotes.
 * @property position Approximate quote position in the original message in UTF-16 code units as specified by the sender
 * @property isManual Optional. True, if the quote was chosen manually by the message sender. Otherwise, the quote was added automatically by the server.
 */
@Serializable
data class TextQuote(
    val text: String,
    val entities: List<MessageEntity>? = null,
    val position: Int,
    val isManual: Boolean? = null,
)
