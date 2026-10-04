package eu.vendeli.tgbot.utils.builders

import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.msg.RichMessageButton
import eu.vendeli.tgbot.types.msg.RichText
import eu.vendeli.tgbot.types.msg.toRichText

/**
 * Marks the rich message DSL builders, so a nested builder cannot silently call functions of an outer one.
 */
@DslMarker
annotation class RichTextDsl

/**
 * Builds a [RichText] from nested formatting nodes.
 *
 * ```kotlin
 * val text = richText {
 *     +"Hello "
 *     bold {
 *         +"big "
 *         italic("world")
 *     }
 *     url("https://example.com", "docs")
 * }
 * ```
 *
 * Every formatting function has three forms: with a [RichText], with a plain [String] and with a nested
 * builder block. Metadata (url, user, ...) always comes before the content.
 */
@RichTextDsl
class RichTextBuilder internal constructor() {
    private val parts = mutableListOf<RichText>()

    private fun add(text: RichText) {
        parts += text
    }

    /** Add plain text. */
    operator fun String.unaryPlus() = add(RichText.Plain(this))

    /** Add an already built [RichText]. */
    operator fun RichText.unaryPlus() = add(this)

    internal fun build(): RichText {
        val flat = parts.flatMap { it.flatten() }
        return when {
            flat.isEmpty() -> RichText.Plain("")
            flat.size == 1 -> flat.first()
            else -> RichText.Chunks(flat)
        }
    }

    /** Adds a [RichText.Bold] (bold) node. */
    fun bold(text: RichText) = add(RichText.Bold(text))
    fun bold(text: String) = add(RichText.Bold(text.toRichText()))
    fun bold(block: RichTextBuilder.() -> Unit) = add(RichText.Bold(richText(block)))

    /** Adds a [RichText.Italic] (italic) node. */
    fun italic(text: RichText) = add(RichText.Italic(text))
    fun italic(text: String) = add(RichText.Italic(text.toRichText()))
    fun italic(block: RichTextBuilder.() -> Unit) = add(RichText.Italic(richText(block)))

    /** Adds a [RichText.Underline] (underlined) node. */
    fun underline(text: RichText) = add(RichText.Underline(text))
    fun underline(text: String) = add(RichText.Underline(text.toRichText()))
    fun underline(block: RichTextBuilder.() -> Unit) = add(RichText.Underline(richText(block)))

    /** Adds a [RichText.Strikethrough] (strikethrough) node. */
    fun strikethrough(text: RichText) = add(RichText.Strikethrough(text))
    fun strikethrough(text: String) = add(RichText.Strikethrough(text.toRichText()))
    fun strikethrough(block: RichTextBuilder.() -> Unit) = add(RichText.Strikethrough(richText(block)))

    /** Adds a [RichText.Spoiler] (spoiler) node. */
    fun spoiler(text: RichText) = add(RichText.Spoiler(text))
    fun spoiler(text: String) = add(RichText.Spoiler(text.toRichText()))
    fun spoiler(block: RichTextBuilder.() -> Unit) = add(RichText.Spoiler(richText(block)))

    /** Adds a [RichText.DateTime] (formatted date and time) node. */
    fun dateTime(unixTime: Long, dateTimeFormat: String, text: RichText) =
        add(RichText.DateTime(text, unixTime, dateTimeFormat))
    fun dateTime(unixTime: Long, dateTimeFormat: String, text: String) =
        add(RichText.DateTime(text.toRichText(), unixTime, dateTimeFormat))
    fun dateTime(unixTime: Long, dateTimeFormat: String, block: RichTextBuilder.() -> Unit) =
        add(RichText.DateTime(richText(block), unixTime, dateTimeFormat))

    /** Adds a [RichText.TextMention] (mention of a user without a username) node. */
    fun textMention(user: User, text: RichText) = add(RichText.TextMention(text, user))
    fun textMention(user: User, text: String) = add(RichText.TextMention(text.toRichText(), user))
    fun textMention(user: User, block: RichTextBuilder.() -> Unit) = add(RichText.TextMention(richText(block), user))

    /** Adds a [RichText.Subscript] (subscript) node. */
    fun subscript(text: RichText) = add(RichText.Subscript(text))
    fun subscript(text: String) = add(RichText.Subscript(text.toRichText()))
    fun subscript(block: RichTextBuilder.() -> Unit) = add(RichText.Subscript(richText(block)))

    /** Adds a [RichText.Superscript] (superscript) node. */
    fun superscript(text: RichText) = add(RichText.Superscript(text))
    fun superscript(text: String) = add(RichText.Superscript(text.toRichText()))
    fun superscript(block: RichTextBuilder.() -> Unit) = add(RichText.Superscript(richText(block)))

    /** Adds a [RichText.Marked] (marked (highlighted)) node. */
    fun marked(text: RichText) = add(RichText.Marked(text))
    fun marked(text: String) = add(RichText.Marked(text.toRichText()))
    fun marked(block: RichTextBuilder.() -> Unit) = add(RichText.Marked(richText(block)))

    /** Adds a [RichText.Code] (code) node. */
    fun code(text: RichText) = add(RichText.Code(text))
    fun code(text: String) = add(RichText.Code(text.toRichText()))
    fun code(block: RichTextBuilder.() -> Unit) = add(RichText.Code(richText(block)))

    /** Adds a [RichText.Url] (link) node. */
    fun url(url: String, text: RichText) = add(RichText.Url(text, url))
    fun url(url: String, text: String) = add(RichText.Url(text.toRichText(), url))
    fun url(url: String, block: RichTextBuilder.() -> Unit) = add(RichText.Url(richText(block), url))

    /** Adds a [RichText.EmailAddress] (email address) node. */
    fun emailAddress(emailAddress: String, text: RichText) = add(RichText.EmailAddress(text, emailAddress))
    fun emailAddress(emailAddress: String, text: String) = add(RichText.EmailAddress(text.toRichText(), emailAddress))
    fun emailAddress(emailAddress: String, block: RichTextBuilder.() -> Unit) =
        add(RichText.EmailAddress(richText(block), emailAddress))

    /** Adds a [RichText.PhoneNumber] (phone number) node. */
    fun phoneNumber(phoneNumber: String, text: RichText) = add(RichText.PhoneNumber(text, phoneNumber))
    fun phoneNumber(phoneNumber: String, text: String) = add(RichText.PhoneNumber(text.toRichText(), phoneNumber))
    fun phoneNumber(phoneNumber: String, block: RichTextBuilder.() -> Unit) =
        add(RichText.PhoneNumber(richText(block), phoneNumber))

    /** Adds a [RichText.BankCardNumber] (bank card number) node. */
    fun bankCardNumber(bankCardNumber: String, text: RichText) = add(RichText.BankCardNumber(text, bankCardNumber))
    fun bankCardNumber(bankCardNumber: String, text: String) =
        add(RichText.BankCardNumber(text.toRichText(), bankCardNumber))
    fun bankCardNumber(bankCardNumber: String, block: RichTextBuilder.() -> Unit) =
        add(RichText.BankCardNumber(richText(block), bankCardNumber))

    /** Adds a [RichText.Mention] (mention of a user by username) node. */
    fun mention(username: String, text: RichText) = add(RichText.Mention(text, username))
    fun mention(username: String, text: String) = add(RichText.Mention(text.toRichText(), username))
    fun mention(username: String, block: RichTextBuilder.() -> Unit) = add(RichText.Mention(richText(block), username))

    /** Adds a [RichText.Hashtag] (hashtag) node. */
    fun hashtag(hashtag: String, text: RichText) = add(RichText.Hashtag(text, hashtag))
    fun hashtag(hashtag: String, text: String) = add(RichText.Hashtag(text.toRichText(), hashtag))
    fun hashtag(hashtag: String, block: RichTextBuilder.() -> Unit) = add(RichText.Hashtag(richText(block), hashtag))

    /** Adds a [RichText.Cashtag] (cashtag) node. */
    fun cashtag(cashtag: String, text: RichText) = add(RichText.Cashtag(text, cashtag))
    fun cashtag(cashtag: String, text: String) = add(RichText.Cashtag(text.toRichText(), cashtag))
    fun cashtag(cashtag: String, block: RichTextBuilder.() -> Unit) = add(RichText.Cashtag(richText(block), cashtag))

    /** Adds a [RichText.BotCommand] (bot command) node. */
    fun botCommand(botCommand: String, text: RichText) = add(RichText.BotCommand(text, botCommand))
    fun botCommand(botCommand: String, text: String) = add(RichText.BotCommand(text.toRichText(), botCommand))
    fun botCommand(botCommand: String, block: RichTextBuilder.() -> Unit) =
        add(RichText.BotCommand(richText(block), botCommand))

    /** Adds a [RichText.AnchorLink] (link to an anchor in the same message) node. */
    fun anchorLink(anchorName: String, text: RichText) = add(RichText.AnchorLink(text, anchorName))
    fun anchorLink(anchorName: String, text: String) = add(RichText.AnchorLink(text.toRichText(), anchorName))
    fun anchorLink(anchorName: String, block: RichTextBuilder.() -> Unit) =
        add(RichText.AnchorLink(richText(block), anchorName))

    /** Adds a [RichText.Reference] (reference) node. */
    fun reference(name: String, text: RichText) = add(RichText.Reference(text, name))
    fun reference(name: String, text: String) = add(RichText.Reference(text.toRichText(), name))
    fun reference(name: String, block: RichTextBuilder.() -> Unit) = add(RichText.Reference(richText(block), name))

    /** Adds a [RichText.ReferenceLink] (link to a reference in the same message) node. */
    fun referenceLink(referenceName: String, text: RichText) = add(RichText.ReferenceLink(text, referenceName))
    fun referenceLink(referenceName: String, text: String) =
        add(RichText.ReferenceLink(text.toRichText(), referenceName))
    fun referenceLink(referenceName: String, block: RichTextBuilder.() -> Unit) =
        add(RichText.ReferenceLink(richText(block), referenceName))

    /** Adds a [RichText.CustomEmoji] (custom emoji) node. */
    fun customEmoji(customEmojiId: String, alternativeText: String) =
        add(RichText.CustomEmoji(customEmojiId, alternativeText))

    /** Adds a [RichText.MathematicalExpression] (inline mathematical expression in LaTeX format) node. */
    fun mathematicalExpression(expression: String) = add(RichText.MathematicalExpression(expression))

    /** Adds a [RichText.Anchor] (anchor) node. */
    fun anchor(name: String) = add(RichText.Anchor(name))

    /** Adds a [RichText.Button] (button) node. */
    fun button(button: RichMessageButton) = add(RichText.Button(button))
}

/**
 * Build a [RichText] using [RichTextBuilder].
 *
 * A single node is returned as is, several nodes are combined into [RichText.Chunks] and an empty block
 * results in an empty plain text.
 */
fun richText(block: RichTextBuilder.() -> Unit): RichText = RichTextBuilder().apply(block).build()
