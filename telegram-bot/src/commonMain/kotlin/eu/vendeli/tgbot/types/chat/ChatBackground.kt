package eu.vendeli.tgbot.types.chat

import eu.vendeli.tgbot.types.BackgroundType
import kotlinx.serialization.Serializable

/**
 * This object represents a chat background.
 *
 * [Api reference](https://core.telegram.org/bots/api#chatbackground)
 * @property type Type of the background
 */
@Serializable
data class ChatBackground(
    val type: BackgroundType,
)
