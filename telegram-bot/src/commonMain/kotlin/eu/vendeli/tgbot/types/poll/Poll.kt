package eu.vendeli.tgbot.types.poll

import eu.vendeli.tgbot.types.msg.MessageEntity
import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlin.time.Instant
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
 * @property questionEntities Optional. Special entities that appear in the question. Currently, only custom emoji entities are allowed in poll questions
 * @property options List of poll options
 * @property totalVoterCount Total number of users that voted in the poll
 * @property isClosed True, if the poll is closed
 * @property isAnonymous True, if the poll is anonymous
 * @property type Poll type, currently can be "regular" or "quiz"
 * @property allowsMultipleAnswers True, if the poll allows multiple answers
 * @property allowsRevoting True, if the poll allows to change the chosen answer options
 * @property correctOptionIds Optional. Array of 0-based identifiers of the correct answer options. Available only for polls in quiz mode which are closed or were sent (not forwarded) by the bot or to the private chat with the bot.
 * @property explanation Optional. Text that is shown when a user chooses an incorrect answer or taps on the lamp icon in a quiz-style poll, 0-200 characters
 * @property explanationEntities Optional. Special entities like usernames, URLs, bot commands, etc. that appear in the explanation
 * @property openPeriod Optional. Amount of time in seconds the poll will be active after creation
 * @property closeDate Optional. Point in time (Unix timestamp) when the poll will be automatically closed
 * @property description Optional. Description of the poll; for polls inside the Message object only
 * @property descriptionEntities Optional. Special entities like usernames, URLs, bot commands, etc. that appear in the description
 * @property media Optional. Media attached to the poll
 * @property explanationMedia Optional. Media attached to the quiz explanation
 * @property membersOnly Optional. True, if the poll allows voting only for members of the chat
 * @property countryCodes Optional. List of two-letter ISO 3166-1 alpha-2 country codes whose users are allowed to vote in the poll. Available only for polls in chats where members must be from the listed countries.
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
    val allowsRevoting: Boolean,
    val correctOptionIds: List<Int>? = null,
    val explanation: String? = null,
    val explanationEntities: List<MessageEntity>? = null,
    val openPeriod: Int? = null,
    @Serializable(InstantSerializer::class)
    val closeDate: Instant? = null,
    val description: String? = null,
    val descriptionEntities: List<MessageEntity>? = null,
    val media: PollMedia? = null,
    val explanationMedia: PollMedia? = null,
    val membersOnly: Boolean = false,
    val countryCodes: List<String>? = null,
)
