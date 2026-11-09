package eu.vendeli.tgbot.types.common

import eu.vendeli.tgbot.types.msg.MessageEntity
import eu.vendeli.tgbot.types.msg.RichText
import kotlinx.serialization.Serializable

/**
 * This object contains information about the quoted part of a message that is replied to by the given message.
 *
 * [Api reference](https://core.telegram.org/bots/api#textquote)
 * @property text Text of the quoted part of a message that is replied to by the given message; may be empty if the quote was chosen from a rich message
 * @property entities Optional. Special entities that appear in the quote. Currently, only bold, italic, underline, strikethrough, spoiler, custom_emoji, and date_time entities are kept in quotes.
 * @property richTexts Optional. Rich text of the quote if it was chosen from a rich message. May contain only plain text, RichTextBold, RichTextItalic, RichTextUnderline, RichTextStrikethrough, RichTextSpoiler, RichTextSubscript, RichTextSuperscript, RichTextDateTime, RichTextCustomEmoji, RichTextMathematicalExpression, and arrays of them.
 * @property position Approximate quote position in the original message in UTF-16 code units as specified by the sender. For quotes from rich messages, it is the position in the text formed by joining plain texts of all quotable texts of the message, separated by line feeds.
 * @property isManual Optional. True, if the quote was chosen manually by the message sender. Otherwise, the quote was added automatically by the server.
 */
@Serializable
data class TextQuote(
    val text: String,
    val entities: List<MessageEntity>? = null,
    val position: Int,
    val isManual: Boolean? = null,
    val richTexts: List<RichText>? = null,
)
