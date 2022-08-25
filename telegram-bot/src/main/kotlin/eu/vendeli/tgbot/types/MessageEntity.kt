package eu.vendeli.tgbot.types

enum class EntityType(private val literal: String) {
    Mention("mention"), Hashtag("hashtag"), Cashtag("cashtag"), BotCommand("bot_command"),
    Url("url"), Email("email"), PhoneNumber("phone_number"), Bold("bold"), Italic("italic"),
    Underline("underline"), Strikethrough("strikethrough"), Spoiler("spoiler"), Code("code"),
    Pre("pre"), TextLink("text_link"), TextMention("text_mention"), CustomEmoji("custom_emoji");

    override fun toString(): String = literal
}

data class MessageEntity(
    val type: EntityType,
    val offset: Int,
    val length: Int,
    val url: String? = null,
    val user: User? = null,
    val language: String? = null,
    val customEmojiId: String? = null,
)
