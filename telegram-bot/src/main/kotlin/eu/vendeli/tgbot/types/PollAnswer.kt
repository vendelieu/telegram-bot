package eu.vendeli.tgbot.types

import eu.vendeli.tgbot.types.chat.Chat
import kotlinx.serialization.Serializable

@Serializable
data class PollAnswer(
    val pollId: String,
    val voterChat: Chat? = null,
    val user: User,
    val optionIds: List<Int>,
)
