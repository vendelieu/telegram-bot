package eu.vendeli.tgbot.types.keyboard

import eu.vendeli.tgbot.types.chat.ChatAdministratorRights
import kotlinx.serialization.Serializable

/**
 * This object defines the criteria used to request a suitable chat. Information about the selected chat will be shared with the bot when the corresponding button is pressed. The bot will be granted requested rights in the сhat if appropriate More about requesting chats: https://core.telegram.org/bots/features#chat-and-user-selection
 *
 * Api reference: https://core.telegram.org/bots/api#keyboardbuttonrequestchat
 * @property requestId Signed 32-bit identifier of the request, which will be received back in the ChatShared object. Must be unique within the message
 * @property chatIsChannel Pass True to request a channel chat, pass False to request a group or a supergroup chat.
 * @property chatIsForum Optional. Pass True to request a forum supergroup, pass False to request a non-forum chat. If not specified, no additional restrictions are applied.
 * @property chatHasUsername Optional. Pass True to request a supergroup or a channel with a username, pass False to request a chat without a username. If not specified, no additional restrictions are applied.
 * @property chatIsCreated Optional. Pass True to request a chat owned by the user. Otherwise, no additional restrictions are applied.
 * @property userAdministratorRights Optional. A JSON-serialized object listing the required administrator rights of the user in the chat. The rights must be a superset of bot_administrator_rights. If not specified, no additional restrictions are applied.
 * @property botAdministratorRights Optional. A JSON-serialized object listing the required administrator rights of the bot in the chat. The rights must be a subset of user_administrator_rights. If not specified, no additional restrictions are applied.
 * @property botIsMember Optional. Pass True to request a chat with the bot as a member. Otherwise, no additional restrictions are applied.
 * @property requestTitle Optional. Pass True to request the chat's title
 * @property requestUsername Optional. Pass True to request the chat's username
 * @property requestPhoto Optional. Pass True to request the chat's photo
 */
@Serializable
data class KeyboardButtonRequestChat(
    val requestId: Int,
    val chatIsChannel: Boolean,
    val chatIsForum: Boolean? = null,
    val chatHasUsername: Boolean? = null,
    val chatIsCreated: Boolean? = null,
    val userAdministratorRights: ChatAdministratorRights? = null,
    val botAdministratorRights: ChatAdministratorRights? = null,
    val botIsMember: Boolean? = null,
    val requestTitle: Boolean? = null,
    val requestUsername: Boolean? = null,
    val requestPhoto: Boolean? = null,
)
