package eu.vendeli.tgbot.utils.builders

import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.media.InputRichMessage
import eu.vendeli.tgbot.types.msg.RichText

class RichEntitiesBuilder(val richTexts: MutableList<RichText>) {
    fun build() = RichText.Chunks(richTexts)
}

fun InputRichMessage.bold(block: () -> String) = RichText.Bold(RichText.Plain(block()))
fun InputRichMessage.italic(block: () -> String) = RichText.Italic(RichText.Plain(block()))
fun InputRichMessage.underline(block: () -> String) = RichText.Underline(RichText.Plain(block()))
fun InputRichMessage.strikethrough(block: () -> String) = RichText.Strikethrough(RichText.Plain(block()))
fun InputRichMessage.spoiler(block: () -> String) = RichText.Spoiler(RichText.Plain(block()))
fun InputRichMessage.dateTime(unixTime: Long, dateTimeFormat: String, block: () -> String) = RichText.DateTime(RichText.Plain(block()), unixTime, dateTimeFormat)
fun InputRichMessage.textMention(user: User, block: () -> String) = RichText.TextMention(RichText.Plain(block()), user)
fun InputRichMessage.subscript(block: () -> String) = RichText.Subscript(RichText.Plain(block()))
fun InputRichMessage.superscript(block: () -> String) = RichText.Superscript(RichText.Plain(block()))
fun InputRichMessage.marked(block: () -> String) = RichText.Marked(RichText.Plain(block()))
fun InputRichMessage.code(block: () -> String) = RichText.Code(RichText.Plain(block()))
fun InputRichMessage.customEmoji(alternativeText: String, block: () -> String) = RichText.CustomEmoji(block(), alternativeText)
fun InputRichMessage.mathematicalExpression(block: () -> String) = RichText.MathematicalExpression(block())
fun InputRichMessage.url(url: String, block: () -> String) = RichText.Url(RichText.Plain(block()), url)
fun InputRichMessage.emailAddress(emailAddress: String, block: () -> String) = RichText.EmailAddress(RichText.Plain(block()), emailAddress)
fun InputRichMessage.phoneNumber(phoneNumber: String, block: () -> String) = RichText.PhoneNumber(RichText.Plain(block()), phoneNumber)
fun InputRichMessage.bankCardNumber(bankCardNumber: String, block: () -> String) = RichText.BankCardNumber(RichText.Plain(block()), bankCardNumber)
fun InputRichMessage.mention(username: String, block: () -> String) = RichText.Mention(RichText.Plain(block()), username)
fun InputRichMessage.hashtag(hashtag: String, block: () -> String) = RichText.Hashtag(RichText.Plain(block()), hashtag)
fun InputRichMessage.cashtag(cashtag: String, block: () -> String) = RichText.Cashtag(RichText.Plain(block()), cashtag)
fun InputRichMessage.botCommand(botCommand: String, block: () -> String) = RichText.BotCommand(RichText.Plain(block()), botCommand)
fun InputRichMessage.anchor(block: () -> String) = RichText.Anchor(block())
fun InputRichMessage.anchorLink(anchorName: String, block: () -> String) = RichText.AnchorLink(RichText.Plain(block()), anchorName)
fun InputRichMessage.reference(reference: String, block: () -> String) = RichText.Reference(RichText.Plain(block()), reference)
fun InputRichMessage.referenceLink(referenceLink: String, block: () -> String) = RichText.ReferenceLink(RichText.Plain(block()), referenceLink)
