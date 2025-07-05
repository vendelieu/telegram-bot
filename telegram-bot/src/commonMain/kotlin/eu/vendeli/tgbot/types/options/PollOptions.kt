package eu.vendeli.tgbot.types.options

import eu.vendeli.tgbot.types.component.ParseMode
import eu.vendeli.tgbot.types.common.ReplyParameters
import eu.vendeli.tgbot.types.msg.MessageEntity
import eu.vendeli.tgbot.types.poll.PollType
import eu.vendeli.tgbot.utils.serde.DurationSerializer
import eu.vendeli.tgbot.utils.serde.InstantSerializer
import kotlin.time.Instant
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class PollOptions(
    var isAnonymous: Boolean? = null,
    var type: PollType? = null,
    var allowsMultipleAnswers: Boolean? = null,
    var correctOptionId: Int? = null,
    var explanation: String? = null,
    var explanationParseMode: ParseMode? = null,
    var explanationEntities: List<MessageEntity>? = null,
    @Serializable(DurationSerializer::class)
    var openPeriod: Duration? = null,
    @Serializable(InstantSerializer::class)
    var closeDate: Instant? = null,
    var isClosed: Boolean? = null,
    var questionParseMode: ParseMode? = null,
    override var disableNotification: Boolean? = null,
    override var replyParameters: ReplyParameters? = null,
    override var protectContent: Boolean? = null,
    override var messageThreadId: Int? = null,
    override var messageEffectId: String? = null,
    override var allowPaidBroadcast: Boolean? = null,
) : OptionsCommon,
    ForumProps,
    MessageEffectIdProp,
    AllowPaidBroadcastProp
