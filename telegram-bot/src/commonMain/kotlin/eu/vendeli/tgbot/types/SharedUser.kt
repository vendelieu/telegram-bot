package eu.vendeli.tgbot.types

import eu.vendeli.tgbot.types.media.PhotoSize
import kotlinx.serialization.Serializable

/**
 * This object contains information about a user that was shared with the bot using a KeyboardButtonRequestUser button.
 *
 * [Api reference](https://core.telegram.org/bots/api#shareduser)
 * @property userId Identifier of the shared user. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so 64-bit integers or double-precision float types are safe for storing these identifiers. The bot may not have access to the user and could be unable to use this identifier, unless the user is already known to the bot by some other means.
 * @property firstName Optional. First name of the user, if the name was requested by the bot
 * @property lastName Optional. Last name of the user, if the name was requested by the bot
 * @property username Optional. Username of the user, if the username was requested by the bot
 * @property photo Optional. Available sizes of the chat photo, if the photo was requested by the bot
 */
@Serializable
data class SharedUser(
    val userId: Long,
    val firstName: String? = null,
    val lastName: String? = null,
    val username: String? = null,
    val photo: List<PhotoSize>? = null,
)
