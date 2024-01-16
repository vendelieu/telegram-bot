package eu.vendeli.tgbot.types.internal.options

import eu.vendeli.tgbot.types.ParseMode
import eu.vendeli.tgbot.types.ReplyParameters

data class PhotoOptions(
    override var parseMode: ParseMode? = null,
    override var disableNotification: Boolean? = null,
    override var replyParameters: ReplyParameters? = null,
    override var protectContent: Boolean? = null,
    override var messageThreadId: Int? = null,
    override var hasSpoiler: Boolean? = null,
) : OptionsCommon, OptionsParseMode, MediaSpoiler
