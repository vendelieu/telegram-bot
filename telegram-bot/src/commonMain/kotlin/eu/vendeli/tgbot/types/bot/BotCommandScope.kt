package eu.vendeli.tgbot.types.bot

import eu.vendeli.tgbot.annotations.internal.TgAPI
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer

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
 * [Api reference](https://core.telegram.org/bots/api#botcommandscope)
 *
 */
@Serializable
sealed class BotCommandScope {
    @OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)
    val type: String by lazy {
        this::class.serializer().descriptor.serialName
    }

    @Serializable
    @SerialName("default")
    data object Default : BotCommandScope()

    @Serializable
    @SerialName("all_private_chats")
    data object AllPrivateChats : BotCommandScope()

    @Serializable
    @SerialName("all_group_chats")
    data object AllGroupChats : BotCommandScope()

    @Serializable
    @SerialName("all_chat_administrators")
    data object AllChatAdministrators : BotCommandScope()

    @Serializable
    @SerialName("chat")
    @TgAPI.Name("BotCommandScopeChat")
    data class ChatScope(
        val chatId: Long,
    ) : BotCommandScope()

    @Serializable
    @SerialName("chat_administrators")
    data class ChatAdministrators(
        val chatId: Long,
    ) : BotCommandScope()

    @Serializable
    @SerialName("chat_member")
    data class ChatMember(
        val chatId: Long,
        val userId: Long,
    ) : BotCommandScope()
}
