package com.github.vendelieu.tgbot.types

data class ChatJoinRequest(
    val chat: Chat,
    val from: User,
    val date: Int,
    val bio: String? = null,
    val inviteLink: ChatInviteLink? = null,
)
