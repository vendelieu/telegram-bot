package eu.vendeli.tgbot.types.gift

import kotlinx.serialization.Serializable

/**
 * This object contains information about the color scheme for a user's name, message replies and link previews based on a unique gift.
 *
 * [Api reference](https://core.telegram.org/bots/api#uniquegiftcolors)
 * @property modelCustomEmojiId Custom emoji identifier of the unique gift's model
 * @property symbolCustomEmojiId Custom emoji identifier of the unique gift's symbol
 * @property lightThemeMainColor Main color used in light themes; RGB format
 * @property lightThemeOtherColors List of 1-3 additional colors used in light themes; RGB format
 * @property darkThemeMainColor Main color used in dark themes; RGB format
 * @property darkThemeOtherColors List of 1-3 additional colors used in dark themes; RGB format
 */
@Serializable
data class UniqueGiftColors(
    val modelCustomEmojiId: String,
    val symbolCustomEmojiId: String,
    val lightThemeMainColor: Int,
    val lightThemeOtherColors: List<Int>,
    val darkThemeMainColor: Int,
    val darkThemeOtherColors: List<Int>,
)

