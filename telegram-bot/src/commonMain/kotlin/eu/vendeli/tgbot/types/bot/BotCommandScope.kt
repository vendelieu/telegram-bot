package eu.vendeli.tgbot.types.bot

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This object represents the scope to which bot commands are applied. Currently, the following 7 scopes are supported:
 * - BotCommandScopeDefault
 * - BotCommandScopeAllPrivateChats
 * - BotCommandScopeAllGroupChats
 * - BotCommandScopeAllChatAdministrators
 * - BotCommandScopeChat
 * - BotCommandScopeChatAdministrators
 * - BotCommandScopeChatMember
 *
 * Api reference: https://core.telegram.org/bots/api#botcommandscope
 *
 */
@Serializable
sealed class BotCommandScope(val type: String) {
    @Serializable
    @SerialName("default")
    class Default : BotCommandScope(type = "default")

    @Serializable
    @SerialName("all_private_chats")
    class AllPrivateChats : BotCommandScope(type = "all_private_chats")

    @Serializable
    @SerialName("all_group_chats")
    class AllGroupChats : BotCommandScope(type = "all_group_chats")

    @Serializable
    @SerialName("all_chat_administrators")
    class AllChatAdministrators : BotCommandScope(type = "all_chat_administrators")

    @Serializable
    @SerialName("chat")
    data class ChatScope(val chatId: Long) : BotCommandScope(type = "chat")

    @Serializable
    @SerialName("chat_administrators")
    data class ChatAdministrators(val chatId: Long) : BotCommandScope(type = "chat_administrators")

    @Serializable
    @SerialName("chat_member")
    data class ChatMember(val chatId: Long, val userId: Long) : BotCommandScope(type = "chat_member")
}
