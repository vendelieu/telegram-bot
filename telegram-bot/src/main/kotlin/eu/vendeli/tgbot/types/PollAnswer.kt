package eu.vendeli.tgbot.types

data class PollAnswer(
    val pollId: String,
    val user: User,
    val optionIds: List<Int>,
)
