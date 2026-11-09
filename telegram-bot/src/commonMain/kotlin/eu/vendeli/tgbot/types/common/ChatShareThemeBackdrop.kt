package eu.vendeli.tgbot.types.common

import eu.vendeli.tgbot.types.gift.UniqueGiftBackdropColors
import kotlinx.serialization.Serializable

/**
 * This object describes a backdrop, which can be used as the background of a theme of the screen for sharing a link to a user or a chat.
 *
 * [Api reference](https://core.telegram.org/bots/api#chatsharethemebackdrop)
 * @property id Unique identifier of the backdrop
 * @property name Name of the backdrop
 * @property colors Colors of the backdrop
 */
@Serializable
data class ChatShareThemeBackdrop(
    val id: Int,
    val name: String,
    val colors: UniqueGiftBackdropColors,
)
