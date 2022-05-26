package com.github.vendelieu.tgbot.types

sealed class BotCommandScope(val type: String) {
    object Default : BotCommandScope(type = "default")
    object AllPrivateChats : BotCommandScope(type = "all_private_chats")
    object AllGroupChats : BotCommandScope(type = "all_group_chats")
    object AllChatAdministrators : BotCommandScope(type = "all_chat_administrators")
    data class Chat(val chatId: Long) : BotCommandScope(type = "chat")
    data class ChatAdministrators(val chatId: Long) : BotCommandScope(type = "chat_administrators")
    data class ChatMember(val chatId: Long, val userId: Long) : BotCommandScope(type = "chat_member")
}
