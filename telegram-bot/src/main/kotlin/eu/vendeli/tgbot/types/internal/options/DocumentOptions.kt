package eu.vendeli.tgbot.types.internal.options

import eu.vendeli.tgbot.types.ParseMode
import eu.vendeli.tgbot.types.ReplyParameters
import eu.vendeli.tgbot.types.internal.ImplicitFile

data class DocumentOptions(
    var thumbnail: ImplicitFile<*>? = null,
    var disableContentTypeDetection: Boolean? = null,
    override var fileName: String? = null,
    override var parseMode: ParseMode? = null,
    override var disableNotification: Boolean? = null,
    override var replyParameters: ReplyParameters? = null,
    override var protectContent: Boolean? = null,
    override var messageThreadId: Int? = null,
) : OptionsCommon, OptionsParseMode, FileOptions
