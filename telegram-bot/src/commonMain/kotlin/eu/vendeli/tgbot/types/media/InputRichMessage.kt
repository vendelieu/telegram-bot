package eu.vendeli.tgbot.types.media

import eu.vendeli.tgbot.types.msg.RichText
import eu.vendeli.tgbot.utils.builders.RichEntitiesBuilder
import kotlinx.serialization.Serializable

/**
 * Describes a rich message to be sent. Exactly one of the fields html, markdown, or blocks must be used.
 *
 * [Api reference](https://core.telegram.org/bots/api#inputrichmessage)
 * @property blocks Optional. Content of the rich message to send described as a list of blocks
 * @property html Optional. Content of the rich message to send described using HTML formatting. See rich message formatting options for more details. Use media field to specify the media used in the message.
 * @property markdown Optional. Content of the rich message to send described using Markdown formatting. See rich message formatting options for more details. Use media field to specify the media used in the message.
 * @property media Optional. List of media that are specified in the markdown or html fields using tg://photo?id=, tg://video?id=, and tg://audio?id= links
 * @property isRtl Optional. Pass True if the rich message must be shown right-to-left
 * @property skipEntityDetection Optional. Pass True to skip automatic detection of entities (e.g., URLs, email addresses, username mentions, hashtags, cashtags, bot commands, or phone numbers) in the text
 */
@Serializable
data class InputRichMessage(
    var blocks: List<InputRichBlock>? = null,
    var html: String? = null,
    var markdown: String? = null,
    var media: List<InputRichMessageMedia>? = null,
    var isRtl: Boolean? = null,
    var skipEntityDetection: Boolean? = null,
) {
    operator fun String.minus(richText: RichText) = RichEntitiesBuilder(mutableListOf(RichText.Plain(this), richText))
    operator fun RichEntitiesBuilder.minus(string: String) = this.apply { richTexts += RichText.Plain(string) }
    operator fun RichEntitiesBuilder.minus(richText: RichText) = this.apply { richTexts += richText }
}
