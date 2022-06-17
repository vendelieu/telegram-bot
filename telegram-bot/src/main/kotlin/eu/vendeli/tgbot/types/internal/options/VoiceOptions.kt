package eu.vendeli.tgbot.types.internal.options

import eu.vendeli.tgbot.types.ParseMode

data class VoiceOptions(
    override var parseMode: ParseMode? = null,
    var duration: Int? = null,
    override var disableNotification: Boolean? = null,
    override var protectContent: Boolean? = null,
    override var replyToMessageId: Long? = null,
    override var allowSendingWithoutReply: Boolean? = null,
) : OptionsInterface<VoiceOptions>, IOptionsCommon, OptionsParseMode
