package eu.vendeli.tgbot.types.common

import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.User
import kotlinx.serialization.Serializable

/**
 * This object represents an answer of a user in a non-anonymous poll.
 *
 * [Api reference](https://core.telegram.org/bots/api#pollanswer)
 * @property pollId Unique poll identifier
 * @property voterChat Optional. The chat that changed the answer to the poll, if the voter is anonymous
 * @property user Optional. The user that changed the answer to the poll, if the voter isn't anonymous
 * @property optionIds 0-based identifiers of chosen answer options. May be empty if the vote was retracted.
 */
@Serializable
data class PollAnswer(
    val pollId: String,
    val voterChat: Chat? = null,
    val user: User? = null,
    val optionIds: List<Int>,
)
