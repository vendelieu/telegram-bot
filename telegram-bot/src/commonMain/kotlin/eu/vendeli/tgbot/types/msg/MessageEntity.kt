package eu.vendeli.tgbot.types.msg

import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
enum class EntityType {
    @SerialName("mention")
    Mention,

    @SerialName("hashtag")
    Hashtag,

    @SerialName("cashtag")
    Cashtag,

    @SerialName("bot_command")
    BotCommand,

    @SerialName("url")
    Url,

    @SerialName("email")
    Email,

    @SerialName("phone_number")
    PhoneNumber,

    @SerialName("bold")
    Bold,

    @SerialName("italic")
    Italic,

    @SerialName("underline")
    Underline,

    @SerialName("strikethrough")
    Strikethrough,

    @SerialName("spoiler")
    Spoiler,

    @SerialName("blockquote")
    Blockquote,

    @SerialName("code")
    Code,

    @SerialName("pre")
    Pre,

    @SerialName("text_link")
    TextLink,

    @SerialName("text_mention")
    TextMention,

    @SerialName("custom_emoji")
    CustomEmoji,

    @SerialName("expandable_blockquote")
    ExpandableBlockQuote,

    @SerialName("date_time")
    DateTime,
}

/**
 * This object represents one special entity in a text message. For example, hashtags, usernames, URLs, etc.
 *
 * [Api reference](https://core.telegram.org/bots/api#messageentity)
 * @property type Type of the entity. Currently, can be "mention" (@username), "hashtag" (#hashtag or #hashtag@chatusername), "cashtag" ($USD or $USD@chatusername), "bot_command" (/start@jobs_bot), "url" (https://telegram.org), "email" (do-not-reply@telegram.org), "phone_number" (+1-212-555-0123), "bold" (bold text), "italic" (italic text), "underline" (underlined text), "strikethrough" (strikethrough text), "spoiler" (spoiler message), "blockquote" (block quotation), "expandable_blockquote" (collapsed-by-default block quotation), "code" (monowidth string), "pre" (monowidth block), "text_link" (for clickable text URLs), "text_mention" (for users without usernames), "custom_emoji" (for inline custom emoji stickers), or "date_time" (for formatted date and time)
 * @property offset Offset in UTF-16 code units to the start of the entity
 * @property length Length of the entity in UTF-16 code units
 * @property url Optional. For "text_link" only, URL that will be opened after user taps on the text
 * @property user Optional. For "text_mention" only, the mentioned user
 * @property language Optional. For "pre" only, the programming language of the entity text
 * @property customEmojiId Optional. For "custom_emoji" only, unique identifier of the custom emoji. Use getCustomEmojiStickers to get full information about the sticker
 * @property unixTime Optional. For "date_time" only, the Unix time associated with the entity
 * @property dateTimeFormat Optional. For "date_time" only, the string that defines the formatting of the date and time. See date-time entity formatting for more details.
 */
@Serializable
data class MessageEntity(
    val type: EntityType,
    val offset: Int,
    val length: Int,
    val url: String? = null,
    val user: User? = null,
    val language: String? = null,
    val customEmojiId: String? = null,
    @Serializable(InstantSerializer::class)
    val unixTime: Instant? = null,
    val dateTimeFormat: String? = null,
)
