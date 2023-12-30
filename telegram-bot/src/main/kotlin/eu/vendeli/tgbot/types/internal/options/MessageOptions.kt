package eu.vendeli.tgbot.types.internal.options

import eu.vendeli.tgbot.types.LinkPreviewOptions
import eu.vendeli.tgbot.types.ParseMode
import eu.vendeli.tgbot.types.ReplyParameters

data class MessageOptions(
    override var linkPreviewOptions: LinkPreviewOptions? = null,
    override var parseMode: ParseMode? = null,
    override var protectContent: Boolean? = null,
    override var disableNotification: Boolean? = null,
    override var replyParameters: ReplyParameters? = null,
    override var messageThreadId: Long? = null,
) : OptionsCommon, OptionsParseMode, LinkPreviewProp
