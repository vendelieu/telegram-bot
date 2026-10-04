package eu.vendeli.tgbot.utils.builders

import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.msg.RichMessageButton
import eu.vendeli.tgbot.types.msg.RichText
import eu.vendeli.tgbot.types.msg.toRichText

// Factories for [RichText] nodes. Metadata comes first and the text last, so a trailing lambda reads naturally
// in the builders and the text can itself be any [RichText] (nesting).

/** Creates a [RichText.Bold] (bold) node around the given [text]. */
fun bold(text: RichText): RichText.Bold = RichText.Bold(text)

/** Creates a [RichText.Bold] (bold) node around the given plain [text]. */
fun bold(text: String): RichText.Bold = RichText.Bold(text.toRichText())

/** Creates a [RichText.Italic] (italic) node around the given [text]. */
fun italic(text: RichText): RichText.Italic = RichText.Italic(text)

/** Creates a [RichText.Italic] (italic) node around the given plain [text]. */
fun italic(text: String): RichText.Italic = RichText.Italic(text.toRichText())

/** Creates a [RichText.Underline] (underlined) node around the given [text]. */
fun underline(text: RichText): RichText.Underline = RichText.Underline(text)

/** Creates a [RichText.Underline] (underlined) node around the given plain [text]. */
fun underline(text: String): RichText.Underline = RichText.Underline(text.toRichText())

/** Creates a [RichText.Strikethrough] (strikethrough) node around the given [text]. */
fun strikethrough(text: RichText): RichText.Strikethrough = RichText.Strikethrough(text)

/** Creates a [RichText.Strikethrough] (strikethrough) node around the given plain [text]. */
fun strikethrough(text: String): RichText.Strikethrough = RichText.Strikethrough(text.toRichText())

/** Creates a [RichText.Spoiler] (spoiler) node around the given [text]. */
fun spoiler(text: RichText): RichText.Spoiler = RichText.Spoiler(text)

/** Creates a [RichText.Spoiler] (spoiler) node around the given plain [text]. */
fun spoiler(text: String): RichText.Spoiler = RichText.Spoiler(text.toRichText())

/** Creates a [RichText.DateTime] (formatted date and time) node around the given [text]. */
fun dateTime(unixTime: Long, dateTimeFormat: String, text: RichText): RichText.DateTime =
    RichText.DateTime(text, unixTime, dateTimeFormat)

/** Creates a [RichText.DateTime] (formatted date and time) node around the given plain [text]. */
fun dateTime(unixTime: Long, dateTimeFormat: String, text: String): RichText.DateTime =
    RichText.DateTime(text.toRichText(), unixTime, dateTimeFormat)

/** Creates a [RichText.TextMention] (mention of a user without a username) node around the given [text]. */
fun textMention(user: User, text: RichText): RichText.TextMention = RichText.TextMention(text, user)

/** Creates a [RichText.TextMention] (mention of a user without a username) node around the given plain [text]. */
fun textMention(user: User, text: String): RichText.TextMention = RichText.TextMention(text.toRichText(), user)

/** Creates a [RichText.Subscript] (subscript) node around the given [text]. */
fun subscript(text: RichText): RichText.Subscript = RichText.Subscript(text)

/** Creates a [RichText.Subscript] (subscript) node around the given plain [text]. */
fun subscript(text: String): RichText.Subscript = RichText.Subscript(text.toRichText())

/** Creates a [RichText.Superscript] (superscript) node around the given [text]. */
fun superscript(text: RichText): RichText.Superscript = RichText.Superscript(text)

/** Creates a [RichText.Superscript] (superscript) node around the given plain [text]. */
fun superscript(text: String): RichText.Superscript = RichText.Superscript(text.toRichText())

/** Creates a [RichText.Marked] (marked (highlighted)) node around the given [text]. */
fun marked(text: RichText): RichText.Marked = RichText.Marked(text)

/** Creates a [RichText.Marked] (marked (highlighted)) node around the given plain [text]. */
fun marked(text: String): RichText.Marked = RichText.Marked(text.toRichText())

/** Creates a [RichText.Code] (code) node around the given [text]. */
fun code(text: RichText): RichText.Code = RichText.Code(text)

/** Creates a [RichText.Code] (code) node around the given plain [text]. */
fun code(text: String): RichText.Code = RichText.Code(text.toRichText())

/** Creates a [RichText.Url] (link) node around the given [text]. */
fun url(url: String, text: RichText): RichText.Url = RichText.Url(text, url)

/** Creates a [RichText.Url] (link) node around the given plain [text]. */
fun url(url: String, text: String): RichText.Url = RichText.Url(text.toRichText(), url)

/** Creates a [RichText.EmailAddress] (email address) node around the given [text]. */
fun emailAddress(emailAddress: String, text: RichText): RichText.EmailAddress =
    RichText.EmailAddress(text, emailAddress)

/** Creates a [RichText.EmailAddress] (email address) node around the given plain [text]. */
fun emailAddress(emailAddress: String, text: String): RichText.EmailAddress =
    RichText.EmailAddress(text.toRichText(), emailAddress)

/** Creates a [RichText.PhoneNumber] (phone number) node around the given [text]. */
fun phoneNumber(phoneNumber: String, text: RichText): RichText.PhoneNumber = RichText.PhoneNumber(text, phoneNumber)

/** Creates a [RichText.PhoneNumber] (phone number) node around the given plain [text]. */
fun phoneNumber(phoneNumber: String, text: String): RichText.PhoneNumber =
    RichText.PhoneNumber(text.toRichText(), phoneNumber)

/** Creates a [RichText.BankCardNumber] (bank card number) node around the given [text]. */
fun bankCardNumber(bankCardNumber: String, text: RichText): RichText.BankCardNumber =
    RichText.BankCardNumber(text, bankCardNumber)

/** Creates a [RichText.BankCardNumber] (bank card number) node around the given plain [text]. */
fun bankCardNumber(bankCardNumber: String, text: String): RichText.BankCardNumber =
    RichText.BankCardNumber(text.toRichText(), bankCardNumber)

/** Creates a [RichText.Mention] (mention of a user by username) node around the given [text]. */
fun mention(username: String, text: RichText): RichText.Mention = RichText.Mention(text, username)

/** Creates a [RichText.Mention] (mention of a user by username) node around the given plain [text]. */
fun mention(username: String, text: String): RichText.Mention = RichText.Mention(text.toRichText(), username)

/** Creates a [RichText.Hashtag] (hashtag) node around the given [text]. */
fun hashtag(hashtag: String, text: RichText): RichText.Hashtag = RichText.Hashtag(text, hashtag)

/** Creates a [RichText.Hashtag] (hashtag) node around the given plain [text]. */
fun hashtag(hashtag: String, text: String): RichText.Hashtag = RichText.Hashtag(text.toRichText(), hashtag)

/** Creates a [RichText.Cashtag] (cashtag) node around the given [text]. */
fun cashtag(cashtag: String, text: RichText): RichText.Cashtag = RichText.Cashtag(text, cashtag)

/** Creates a [RichText.Cashtag] (cashtag) node around the given plain [text]. */
fun cashtag(cashtag: String, text: String): RichText.Cashtag = RichText.Cashtag(text.toRichText(), cashtag)

/** Creates a [RichText.BotCommand] (bot command) node around the given [text]. */
fun botCommand(botCommand: String, text: RichText): RichText.BotCommand = RichText.BotCommand(text, botCommand)

/** Creates a [RichText.BotCommand] (bot command) node around the given plain [text]. */
fun botCommand(botCommand: String, text: String): RichText.BotCommand =
    RichText.BotCommand(text.toRichText(), botCommand)

/** Creates a [RichText.AnchorLink] (link to an anchor in the same message) node around the given [text]. */
fun anchorLink(anchorName: String, text: RichText): RichText.AnchorLink = RichText.AnchorLink(text, anchorName)

/** Creates a [RichText.AnchorLink] (link to an anchor in the same message) node around the given plain [text]. */
fun anchorLink(anchorName: String, text: String): RichText.AnchorLink =
    RichText.AnchorLink(text.toRichText(), anchorName)

/** Creates a [RichText.Reference] (reference) node around the given [text]. */
fun reference(name: String, text: RichText): RichText.Reference = RichText.Reference(text, name)

/** Creates a [RichText.Reference] (reference) node around the given plain [text]. */
fun reference(name: String, text: String): RichText.Reference = RichText.Reference(text.toRichText(), name)

/** Creates a [RichText.ReferenceLink] (link to a reference in the same message) node around the given [text]. */
fun referenceLink(referenceName: String, text: RichText): RichText.ReferenceLink =
    RichText.ReferenceLink(text, referenceName)

/** Creates a [RichText.ReferenceLink] (link to a reference in the same message) node around the given plain [text]. */
fun referenceLink(referenceName: String, text: String): RichText.ReferenceLink =
    RichText.ReferenceLink(text.toRichText(), referenceName)

/** Creates a [RichText.CustomEmoji] (custom emoji) node. */
fun customEmoji(customEmojiId: String, alternativeText: String): RichText.CustomEmoji =
    RichText.CustomEmoji(customEmojiId, alternativeText)

/** Creates a [RichText.MathematicalExpression] (inline mathematical expression in LaTeX format) node. */
fun mathematicalExpression(expression: String): RichText.MathematicalExpression =
    RichText.MathematicalExpression(expression)

/** Creates a [RichText.Anchor] (anchor) node. */
fun anchor(name: String): RichText.Anchor = RichText.Anchor(name)

/** Creates a [RichText.Button] (button) node. */
fun button(button: RichMessageButton): RichText.Button = RichText.Button(button)
