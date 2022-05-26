package com.github.vendelieu.tgbot.types.internal.options

import com.github.vendelieu.tgbot.types.ParseMode

data class CopyMessageOptions(
    override var disableNotification: Boolean? = null,
    override var protectContent: Boolean? = null,
    override var parseMode: ParseMode? = null,
    override var replyToMessageId: Long? = null,
    override var allowSendingWithoutReply: Boolean? = null,
) : OptionsInterface<CopyMessageOptions>, OptionsParseMode, IOptionsCommon
