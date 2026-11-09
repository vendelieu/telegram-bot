package eu.vendeli.tgbot.types.common

import eu.vendeli.tgbot.interfaces.marker.MultipleResponse
import kotlinx.serialization.Serializable

/**
 * This object contains a list of backdrops, which can be used as the background of a theme of the screen for sharing a link to a user or a chat.
 *
 * [Api reference](https://core.telegram.org/bots/api#chatsharethemebackdrops)
 * @property backdrops The list of backdrops
 */
@Serializable
data class ChatShareThemeBackdrops(
    val backdrops: List<ChatShareThemeBackdrop>,
) : MultipleResponse
