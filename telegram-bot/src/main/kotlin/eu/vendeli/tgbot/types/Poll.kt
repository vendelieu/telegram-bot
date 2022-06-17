package eu.vendeli.tgbot.types

enum class PollType(private val literal: String) {
    Regular("regular"), Quiz("quiz");

    override fun toString(): String = literal
}

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
    val closeDate: Int?,
)
