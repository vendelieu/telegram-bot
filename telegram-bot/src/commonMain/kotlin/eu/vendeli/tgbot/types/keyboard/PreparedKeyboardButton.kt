package eu.vendeli.tgbot.types.keyboard

import kotlinx.serialization.Serializable

/**
 * Describes a keyboard button to be used by a user of a Mini App.
 *
 * [Api reference](https://core.telegram.org/bots/api#preparedkeyboardbutton)
 * @property id Unique identifier of the keyboard button
 */
@Serializable
data class PreparedKeyboardButton(
    val id: String,
)
