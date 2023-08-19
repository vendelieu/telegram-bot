package eu.vendeli.tgbot.types

import eu.vendeli.tgbot.types.chat.Chat

data class PollAnswer(
    val pollId: String,
    val voterChat: Chat? = null,
    val user: User,
    val optionIds: List<Int>,
)
