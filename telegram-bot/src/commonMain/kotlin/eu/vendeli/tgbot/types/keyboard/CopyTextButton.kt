package eu.vendeli.tgbot.types.keyboard

import kotlinx.serialization.Serializable

/**
 * This object represents an inline keyboard button that copies specified text to the clipboard.
 *
 * [Api reference](https://core.telegram.org/bots/api#copytextbutton)
 * @property text The text to be copied to the clipboard; 1-256 characters
 */
@Serializable
data class CopyTextButton(
    val text: String,
)
