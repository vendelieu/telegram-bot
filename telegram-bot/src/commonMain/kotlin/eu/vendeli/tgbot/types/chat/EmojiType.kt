package eu.vendeli.tgbot.types.chat

import kotlinx.serialization.*

/**
 * This object describes an emoji. Currently, it can be one of
 * - EmojiTypeEmoji
 * - EmojiTypeCustomEmoji
 *
 * [Api reference](https://core.telegram.org/bots/api#emojitype)
 *
 */
@Serializable
sealed class EmojiType {
    @OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)
    val type: String by lazy {
        this::class.serializer().descriptor.serialName
    }

    /**
     * A regular emoji.
     * @property emoji The emoji
     */
    @Serializable
    @SerialName("emoji")
    data class Emoji(
        val emoji: String,
    ) : EmojiType()

    /**
     * A custom emoji.
     * @property customEmojiId Identifier of the custom emoji. Use getCustomEmojiStickers to get full information about the sticker.
     */
    @Serializable
    @SerialName("custom_emoji")
    data class CustomEmoji(
        val customEmojiId: String,
    ) : EmojiType()
}
