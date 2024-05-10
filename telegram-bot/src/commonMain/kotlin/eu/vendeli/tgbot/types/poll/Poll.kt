package eu.vendeli.tgbot.types.poll

import eu.vendeli.tgbot.types.MessageEntity
import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PollType {
    @SerialName("regular")
    Regular,

    @SerialName("quiz")
    Quiz,
}

/**
 * This object contains information about a poll.
 *
 * [Api reference](https://core.telegram.org/bots/api#poll)
 * @property id Unique poll identifier
 * @property question Poll question, 1-300 characters
 * @property options List of poll options
 * @property totalVoterCount Total number of users that voted in the poll
 * @property isClosed True, if the poll is closed
 * @property isAnonymous True, if the poll is anonymous
 * @property type Poll type, currently can be "regular" or "quiz"
 * @property allowsMultipleAnswers True, if the poll allows multiple answers
 * @property correctOptionId Optional. 0-based identifier of the correct answer option. Available only for polls in the quiz mode, which are closed, or was sent (not forwarded) by the bot or to the private chat with the bot.
 * @property explanation Optional. Text that is shown when a user chooses an incorrect answer or taps on the lamp icon in a quiz-style poll, 0-200 characters
 * @property explanationEntities Optional. Special entities like usernames, URLs, bot commands, etc. that appear in the explanation
 * @property openPeriod Optional. Amount of time in seconds the poll will be active after creation
 * @property closeDate Optional. Point in time (Unix timestamp) when the poll will be automatically closed
 */
@Serializable
data class Poll(
    val id: String,
    val question: String,
    val options: List<PollOption>,
    val questionEntities: List<MessageEntity>? = null,
    val totalVoterCount: Int,
    val isClosed: Boolean,
    val isAnonymous: Boolean,
    val type: PollType,
    val allowsMultipleAnswers: Boolean,
    val correctOptionId: Int? = null,
    val explanation: String? = null,
    val explanationEntities: List<MessageEntity>? = null,
    val openPeriod: Int? = null,
    @Serializable(InstantSerializer::class)
    val closeDate: Instant? = null,
)
