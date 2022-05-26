package com.github.vendelieu.tgbot.types.internal.options

import com.github.vendelieu.tgbot.types.ParseMode

data class MessageOptions(
    var disableWebPagePreview: Boolean? = null,
    override var parseMode: ParseMode? = null,
    override var protectContent: Boolean? = null,
    override var disableNotification: Boolean? = null,
    override var replyToMessageId: Long? = null,
    override var allowSendingWithoutReply: Boolean? = null,
) : OptionsInterface<MessageOptions>, IOptionsCommon, OptionsParseMode
