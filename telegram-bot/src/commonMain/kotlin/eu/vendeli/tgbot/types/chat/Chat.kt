package eu.vendeli.tgbot.types.chat

import eu.vendeli.tgbot.types.internal.IdLong
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ChatType {
    @SerialName("private")
    Private,

    @SerialName("group")
    Group,

    @SerialName("supergroup")
    Supergroup,

    @SerialName("channel")
    Channel,

    @SerialName("sender")
    Sender,
}

/**
 * This object represents a chat.
 *
 * [Api reference](https://core.telegram.org/bots/api#chat)
 * @property id Unique identifier for this chat. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this identifier.
 * @property type Type of the chat, can be either "private", "group", "supergroup" or "channel"
 * @property title Optional. Title, for supergroups, channels and group chats
 * @property username Optional. Username, for private chats, supergroups and channels if available
 * @property firstName Optional. First name of the other party in a private chat
 * @property lastName Optional. Last name of the other party in a private chat
 * @property isForum Optional. True, if the supergroup chat is a forum (has topics enabled)
 */
@Serializable
data class Chat(
    override val id: Long,
    val type: ChatType,
    val title: String? = null,
    val username: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val isForum: Boolean? = null,
) : IdLong {
    val fullName = (firstName?.plus(" ") ?: "") + (lastName ?: "")
}
