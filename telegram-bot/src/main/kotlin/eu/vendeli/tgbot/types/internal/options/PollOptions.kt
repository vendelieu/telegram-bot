package eu.vendeli.tgbot.types.internal.options

import eu.vendeli.tgbot.types.MessageEntity
import eu.vendeli.tgbot.types.ParseMode
import eu.vendeli.tgbot.types.PollType

data class PollOptions(
    var isAnonymous: Boolean? = null,
    var type: PollType? = null,
    var allowsMultipleAnswers: Boolean? = null,
    var correctOptionId: Int? = null,
    var explanation: String? = null,
    var explanationParseMode: ParseMode? = null,
    var explanationEntities: List<MessageEntity>? = null,
    var openPeriod: Int? = null,
    var closeDate: Int? = null,
    var isClosed: Boolean? = null,
    override var disableNotification: Boolean? = null,
    override var replyToMessageId: Long? = null,
    override var allowSendingWithoutReply: Boolean? = null,
    override var protectContent: Boolean? = null,
) : OptionsInterface<PollOptions>, OptionsCommon
