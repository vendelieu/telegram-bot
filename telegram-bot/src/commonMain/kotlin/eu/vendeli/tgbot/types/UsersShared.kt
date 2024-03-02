package eu.vendeli.tgbot.types

import kotlinx.serialization.Serializable

/**
 * This object contains information about the users whose identifiers were shared with the bot using a KeyboardButtonRequestUsers button.
 * @property requestId Identifier of the request
 * @property userIds Identifiers of the shared users. These numbers may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting them. But they have at most 52 significant bits, so 64-bit integers or double-precision float types are safe for storing these identifiers. The bot may not have access to the users and could be unable to use these identifiers, unless the users are already known to the bot by some other means.
 * Api reference: https://core.telegram.org/bots/api#usersshared
*/
@Serializable
data class UsersShared(
    val requestId: Int,
    val userId: List<Long>,
)
