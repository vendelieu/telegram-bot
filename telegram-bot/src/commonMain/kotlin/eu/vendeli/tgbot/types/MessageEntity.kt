package eu.vendeli.tgbot.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
}

@Serializable
data class MessageEntity(
    val type: EntityType,
    val offset: Int,
    val length: Int,
    val url: String? = null,
    val user: User? = null,
    val language: String? = null,
    val customEmojiId: String? = null,
)
