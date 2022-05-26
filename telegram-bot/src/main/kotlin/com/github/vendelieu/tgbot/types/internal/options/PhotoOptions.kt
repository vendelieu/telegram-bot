package com.github.vendelieu.tgbot.types.internal.options

import com.github.vendelieu.tgbot.types.ParseMode

data class PhotoOptions(
    override var parseMode: ParseMode? = null,
    override var disableNotification: Boolean? = null,
    override var replyToMessageId: Long? = null,
    override var allowSendingWithoutReply: Boolean? = null,
    override var protectContent: Boolean? = null,
) : OptionsInterface<PhotoOptions>, IOptionsCommon, OptionsParseMode
