package com.github.vendelieu.tgbot.types

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type",
    defaultImpl = BotCommandScope.Default::class
)
@JsonSubTypes(
    JsonSubTypes.Type(value = BotCommandScope.Default::class, name = "default"),
    JsonSubTypes.Type(value = BotCommandScope.AllPrivateChats::class, name = "all_private_chats"),
    JsonSubTypes.Type(value = BotCommandScope.AllGroupChats::class, name = "all_group_chats"),
    JsonSubTypes.Type(value = BotCommandScope.AllChatAdministrators::class, name = "all_chat_administrators"),
    JsonSubTypes.Type(value = BotCommandScope.Chat::class, name = "chat"),
    JsonSubTypes.Type(value = BotCommandScope.ChatAdministrators::class, name = "chat_administrators"),
    JsonSubTypes.Type(value = BotCommandScope.ChatMember::class, name = "chat_member"),
)
sealed class BotCommandScope(val type: String) {
    object Default : BotCommandScope(type = "default")
    object AllPrivateChats : BotCommandScope(type = "all_private_chats")
    object AllGroupChats : BotCommandScope(type = "all_group_chats")
    object AllChatAdministrators : BotCommandScope(type = "all_chat_administrators")
    data class Chat(val chatId: Long) : BotCommandScope(type = "chat")
    data class ChatAdministrators(val chatId: Long) : BotCommandScope(type = "chat_administrators")
    data class ChatMember(val chatId: Long, val userId: Long) : BotCommandScope(type = "chat_member")
}
