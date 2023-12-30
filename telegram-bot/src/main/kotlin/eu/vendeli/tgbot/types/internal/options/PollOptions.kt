package eu.vendeli.tgbot.types.internal.options

import eu.vendeli.tgbot.types.MessageEntity
import eu.vendeli.tgbot.types.ParseMode
import eu.vendeli.tgbot.types.PollType
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
    var openPeriod: Duration? = null,
    var closeDate: Instant? = null,
    var isClosed: Boolean? = null,
    override var disableNotification: Boolean? = null,
    override var replyToMessageId: Long? = null,
    override var allowSendingWithoutReply: Boolean? = null,
    override var protectContent: Boolean? = null,
    override var messageThreadId: Long? = null,
) : OptionsCommon
