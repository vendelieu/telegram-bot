package eu.vendeli.tgbot.types

import kotlinx.serialization.Serializable

/**
 * This object contains information about the users whose identifiers were shared with the bot using a KeyboardButtonRequestUsers button.
 *
 * [Api reference](https://core.telegram.org/bots/api#usersshared)
 * @property requestId Identifier of the request
 * @property users Information about users shared with the bot.
 */
@Serializable
data class UsersShared(
    val requestId: Int,
    val users: List<SharedUser>,
)
