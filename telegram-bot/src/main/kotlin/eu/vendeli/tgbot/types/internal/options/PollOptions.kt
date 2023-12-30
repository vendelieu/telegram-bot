package eu.vendeli.tgbot.types.internal.options

import com.fasterxml.jackson.annotation.JsonFormat
import eu.vendeli.tgbot.types.MessageEntity
import eu.vendeli.tgbot.types.ParseMode
import eu.vendeli.tgbot.types.PollType
import eu.vendeli.tgbot.types.ReplyParameters
import java.time.Instant
import kotlin.time.Duration

data class PollOptions(
    var isAnonymous: Boolean? = null,
    var type: PollType? = null,
    var allowsMultipleAnswers: Boolean? = null,
    var correctOptionId: Int? = null,
    var explanation: String? = null,
    var explanationParseMode: ParseMode? = null,
    var explanationEntities: List<MessageEntity>? = null,
    @field:JsonFormat(pattern = "SECONDS")
    var openPeriod: Duration? = null,
    var closeDate: Instant? = null,
    var isClosed: Boolean? = null,
    override var disableNotification: Boolean? = null,
    override var replyParameters: ReplyParameters? = null,
    override var protectContent: Boolean? = null,
    override var messageThreadId: Long? = null,
) : OptionsCommon
