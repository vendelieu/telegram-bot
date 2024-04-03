package eu.vendeli.tgbot.types.chat

import eu.vendeli.tgbot.types.media.PhotoSize
import kotlinx.serialization.Serializable

/**
 * This object contains information about a chat that was shared with the bot using a KeyboardButtonRequestChat button.
 *
 * Api reference: https://core.telegram.org/bots/api#chatshared
 * @property requestId Identifier of the request
 * @property chatId Identifier of the shared chat. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a 64-bit integer or double-precision float type are safe for storing this identifier. The bot may not have access to the chat and could be unable to use this identifier, unless the chat is already known to the bot by some other means.
 * @property title Optional. Title of the chat, if the title was requested by the bot.
 * @property username Optional. Username of the chat, if the username was requested by the bot and available.
 * @property photo Optional. Available sizes of the chat photo, if the photo was requested by the bot
 */
@Serializable
data class ChatShared(
    val requestId: Int,
    val chatId: Long,
    val title: String? = null,
    val username: String? = null,
    val photo: List<PhotoSize>? = null,
)
