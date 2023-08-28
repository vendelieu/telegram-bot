package eu.vendeli.tgbot.types.internal.options

import eu.vendeli.tgbot.types.ParseMode

data class CopyMessageOptions(
    override var disableNotification: Boolean? = null,
    override var protectContent: Boolean? = null,
    override var parseMode: ParseMode? = null,
    override var replyToMessageId: Long? = null,
    override var allowSendingWithoutReply: Boolean? = null,
    override var messageThreadId: Long? = null,
) : OptionsParseMode, OptionsCommon
