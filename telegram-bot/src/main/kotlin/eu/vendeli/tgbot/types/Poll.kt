package eu.vendeli.tgbot.types

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

@Serializable
data class Poll(
    val id: String,
    val question: String,
    val options: List<PollOption>,
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
