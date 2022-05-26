package com.github.vendelieu.tgbot.types.internal.options

import com.github.vendelieu.tgbot.types.ParseMode

data class VoiceOptions(
    override var parseMode: ParseMode? = null,
    var duration: Int? = null,
    override var disableNotification: Boolean? = null,
    override var protectContent: Boolean? = null,
    override var replyToMessageId: Long? = null,
    override var allowSendingWithoutReply: Boolean? = null,
) : OptionsInterface<VoiceOptions>, IOptionsCommon, OptionsParseMode
