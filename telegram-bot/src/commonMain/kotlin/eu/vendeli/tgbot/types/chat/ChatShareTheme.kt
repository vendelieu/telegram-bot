package eu.vendeli.tgbot.types.chat

import eu.vendeli.tgbot.annotations.internal.TgAPI
import eu.vendeli.tgbot.types.gift.UniqueGift
import kotlinx.serialization.*

/**
 * This object describes the theme of the screen for sharing a link to a user or a chat. Currently, it can be one of
 * - ChatShareThemeEmoji
 * - ChatShareThemeUniqueGift
 *
 * [Api reference](https://core.telegram.org/bots/api#chatsharetheme)
 *
 */
@Serializable
sealed class ChatShareTheme {
    @OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)
    val type: String by lazy {
        this::class.serializer().descriptor.serialName
    }

    /**
     * The theme with emoji placed on a background.
     * @property emojis The emoji; 0-6 regular or custom emoji
     * @property backgroundBackdropId Optional. Identifier of the backdrop used as the background; omitted if the default background is used. Use getChatShareThemeBackdrops to get details about the backdrop.
     */
    @Serializable
    @SerialName("emoji")
    data class Emoji(
        val emojis: List<EmojiType>,
        val backgroundBackdropId: Int? = null,
    ) : ChatShareTheme()

    /**
     * The theme based on a unique gift owned by the user or the chat.
     * @property gift The gift
     */
    @Serializable
    @SerialName("unique_gift")
    @TgAPI.Name("ChatShareThemeUniqueGift")
    data class UniqueGiftTheme(
        val gift: UniqueGift,
    ) : ChatShareTheme()
}
